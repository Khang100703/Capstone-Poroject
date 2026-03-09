package com;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OPCUA {

    private OpcUaClient client;

    // --- 1. KẾT NỐI KEPWARE (Port 49320) ---
    public void connect() {
        try {
            if (client == null) {
                // Endpoint chuẩn của Kepware. Thay localhost bằng IP máy Kepware nếu chạy khác máy.
                client = OpcUaClient.create("opc.tcp://localhost:49320");
                client.connect().get();
                System.out.println("--- KẾT NỐI KEPWARE THÀNH CÔNG ---");
            }
        } catch (Exception e) {
            System.err.println("Lỗi kết nối Kepware: " + e.getMessage());
        }
    }

    // --- 2. ĐỌC NHIỀU TAG CÙNG LÚC (Bulk Read - Tối ưu tốc độ) ---
    public Map<String, Object> readListTags(List<String> tagNames) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (client == null) connect();

            // Bước 2.1: Tạo danh sách NodeId từ tên Tag ngắn gọn
            // Cấu trúc Kepware: ns=2;s=Channel1.Device1.<Tên_Tag>
            List<NodeId> nodeIds = tagNames.stream()
                    .map(name -> new NodeId(2, "Channel1.Device1." + name))
                    .collect(Collectors.toList());

            // Bước 2.2: Gửi 1 yêu cầu duy nhất để lấy toàn bộ dữ liệu (Rất nhanh)
            List<DataValue> values = client.readValues(0, TimestampsToReturn.Both, nodeIds).get();

            // Bước 2.3: Ghép kết quả trả về vào Map tương ứng với tên Tag
            for (int i = 0; i < tagNames.size(); i++) {
                DataValue dv = values.get(i);
                // Nếu giá trị null thì gán là 0 hoặc null để tránh lỗi Web
                Object val = (dv.getValue() != null) ? dv.getValue().getValue() : 0;
                result.put(tagNames.get(i), val);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu mất kết nối, trả về null hết để Web biết
            for (String tag : tagNames) result.put(tag, null);
        }
        return result;
    }

    // --- 3. GHI DỮ LIỆU (Dùng cho nút Start/Stop/SetSpeed) ---
    public void writeTag(String tagName, Object value) {
        try {
            if (client == null) connect();

            NodeId nodeId = new NodeId(2, "Channel1.Device1." + tagName);
            Variant v = new Variant(value);
            DataValue dataValue = new DataValue(v, null, null);

            client.writeValue(nodeId, dataValue).get();
            System.out.println("Đã ghi xuống [" + tagName + "]: " + value);
        } catch (Exception e) {
            System.err.println("Lỗi ghi tag [" + tagName + "]: " + e.getMessage());
        }
    }
    // --- THÊM HÀM NÀY VÀO FILE OPCUA.java ---
    public String readTag(String tagName) {
        try {
            // Tận dụng hàm readListTags có sẵn để đọc 1 cái
            // Cách này an toàn vì nó tự động xử lý việc map tên Tag (Short Name)
            java.util.List<String> singleList = java.util.Collections.singletonList(tagName);
            java.util.Map<String, Object> result = readListTags(singleList);

            if (result != null && result.containsKey(tagName)) {
                return result.get(tagName).toString();
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc tag lẻ: " + tagName);
        }
        return "0"; // Trả về 0 nếu lỗi hoặc không tìm thấy
    }
}
