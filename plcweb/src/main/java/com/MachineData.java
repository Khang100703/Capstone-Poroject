package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MachineData {

    public static String currentProductCode = "WAITING...";
    private static Double lastTorque = 0.0;
    @Autowired
    private OPCUA opcService;

    @Autowired
    private MachineLogRepository logRepo;

    // =========================
    // API ĐỌC DỮ LIỆU REALTIME
    // =========================
    @GetMapping("/machine-data")
    public Map<String, Object> getMachineData() {

        List<String> tagsToRead = List.of(
                "Yellow Light",
                "Green Light",
                "Red Light",
                "Invt Run",
                "Invt Error",
                "Output Power",
                "Output Torque",
                "Output Voltage",
                "Output Current",
                "Operation Speed",
                "Operation Frequency",
                "Spindle Speed",
                "Spindle Temperature",
                "Motor Temperature",
                "PID Error",
                "Momen HMI",
                "Kp",
                "Ki",
                "Kd",
                "Current sensor value",
                "Bus Voltage",
                "Stop",
                "Run Reverse",
                "Run Forward",
                "Reverse Button",
                "Forward Button",
                "Power Stop Button",
                "Power Start Button",
                "Power Start",
                "Buzzer",
                "Brake",
                "HMI Stop",
                "Start HMI",
                "Reset HMI",
                "Set Point",
                "Setting Frequency"
        );

        Map<String, Object> data = opcService.readListTags(tagsToRead);

        if (data == null) {
            data = new HashMap<>();
        }

        data.put("product_code", currentProductCode);

        return data;
    }
    private void pulseTag(String tagName) {
    new Thread(() -> {
        try {
            opcService.writeTag(tagName, true);   // nhấn
            Thread.sleep(120);                    // giữ 120 ms
            opcService.writeTag(tagName, false);  // nhả
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
}

    // =========================
    // API GỬI LỆNH ĐIỀU KHIỂN
    // =========================
    @PostMapping("/control")
    public String sendCommand(@RequestParam String cmd,
                              @RequestParam(defaultValue = "0") double val) {

        try {
            switch (cmd) {

                case "START":
                 pulseTag("Start HMI");
                 break;

                case "STOP":
                pulseTag("HMI Stop");
                break;

                case "RESET":
                pulseTag("Reset HMI");
                break;

                case "SET_SPEED":
                    opcService.writeTag("Set Point", (short) Math.round(val));
                    break;

                case "SET_TORQUE":
                    opcService.writeTag("Momen HMI", (float) val);
                    break;

                case "SET_FREQ":
                    opcService.writeTag("Setting Frequency", (float) val);
                    break;

                default:
                    return "ERROR: Unknown Command";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        return "OK";
    }

    // =========================
    // AUTO SAVE LOG (KHÔNG XÓA)
    // =========================
    @Scheduled(fixedRate = 5000)
    
    public void autoSaveLog() {
        try {
            Double runFreq = convert(opcService.readTag("Operation Frequency"));
            Double setFreq = convert(opcService.readTag("Setting Frequency"));
            Double busVolt = convert(opcService.readTag("Bus Voltage"));
            Double outVolt = convert(opcService.readTag("Output Voltage"));
            Double outCurr = convert(opcService.readTag("Output Current"));
            Double runSpeed = convert(opcService.readTag("Operation Speed"));
            Double outPower = convert(opcService.readTag("Output Power"));
            Double outTorque = convert(opcService.readTag("Output Torque"));
            if (outTorque != null) {
            if (Math.abs(outTorque - lastTorque) > 20) {
             outTorque = lastTorque;   // bỏ spike
         }
    lastTorque = outTorque;
}

            MachineLog log = new MachineLog(
                    currentProductCode,
                    runFreq,
                    setFreq,
                    busVolt,
                    outVolt,
                    outCurr,
                    runSpeed,
                    outPower,
                    outTorque
            );

            logRepo.save(log);

            System.out.println(">> [AUTO LOG] Saved speed=" + runSpeed);

        } catch (Exception e) {
            System.err.println("Lỗi lưu log: " + e.getMessage());
        }
    }

    // =========================
    // LOAD PRODUCT FROM RECIPE
    // =========================
    @PostMapping("/load-product")
    public String loadProduct(@RequestParam String code) {
        currentProductCode = code;
        System.out.println(">> Loaded product: " + code);
        return "OK";
    }

    // =========================
    // API REPORT SEARCH
    // =========================
    @GetMapping("/reports/search")
    public List<MachineLog> searchReports(
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            var formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            var startDate = java.time.LocalDateTime.parse(from, formatter);
            var endDate = java.time.LocalDateTime.parse(to, formatter);

            return logRepo.findByRecordedAtBetween(startDate, endDate);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // =========================
    // HÀM CONVERT AN TOÀN
    // =========================
    private Double convert(String val) {
        try {
            if (val == null || val.isEmpty() || val.equals("null")) return 0.0;
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
