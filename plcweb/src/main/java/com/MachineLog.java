package com;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "machine_logs")
public class MachineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Các cột dữ liệu (Khớp với SQL bạn vừa tạo)
    private String productCode; // Mã sản phẩm hiện tại
    private Double runFreq;       // run_freq
    private Double setFreq;       // set_freq
    private Double busVoltage;    // bus_voltage
    private Double outputVoltage; // output_voltage
    private Double outputCurrent; // output_current
    private Double runSpeed;      // run_speed
    private Double outputPower;   // output_power
    private Double outputTorque;  // output_torque

    private LocalDateTime recordedAt = LocalDateTime.now(); // Thời gian hiện tại

   
    public MachineLog() {}

    
    public MachineLog(String productCode,Double runFreq, Double setFreq, Double busVoltage, Double outputVoltage, 
                      Double outputCurrent, Double runSpeed, Double outputPower, Double outputTorque) {
        this.productCode = productCode;
        this.runFreq = runFreq;
        this.setFreq = setFreq;
        this.busVoltage = busVoltage;
        this.outputVoltage = outputVoltage;
        this.outputCurrent = outputCurrent;
        this.runSpeed = runSpeed;
        this.outputPower = outputPower;
        this.outputTorque = outputTorque;
    }

    // --- GETTER & SETTER 
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getRunFreq() { return runFreq; }
    public void setRunFreq(Double runFreq) { this.runFreq = runFreq; }
    public Double getSetFreq() { return setFreq; }
    public void setSetFreq(Double setFreq) { this.setFreq = setFreq; }
    public Double getBusVoltage() { return busVoltage; }
    public void setBusVoltage(Double busVoltage) { this.busVoltage = busVoltage; }
    public Double getOutputVoltage() { return outputVoltage; }
    public void setOutputVoltage(Double outputVoltage) { this.outputVoltage = outputVoltage; }
    public Double getOutputCurrent() { return outputCurrent; }
    public void setOutputCurrent(Double outputCurrent) { this.outputCurrent = outputCurrent; }
    public Double getRunSpeed() { return runSpeed; }
    public void setRunSpeed(Double runSpeed) { this.runSpeed = runSpeed; }
    public Double getOutputPower() { return outputPower; }
    public void setOutputPower(Double outputPower) { this.outputPower = outputPower; }
    public Double getOutputTorque() { return outputTorque; }
    public void setOutputTorque(Double outputTorque) { this.outputTorque = outputTorque; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}  