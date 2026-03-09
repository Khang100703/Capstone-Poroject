package com;

import jakarta.persistence.*; // Import cái này

@Entity
@Table(name = "recipes") // Tên bảng trong MySQL
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    private Long id;

    private String name;
    private Double speed;
    private Double torque;
    private Double accel;
    private Double cycle;

    public Recipe() {} // Bắt buộc

    // Constructor có tham số
    public Recipe(String name, Double speed, Double torque, Double accel, Double cycle) {
        this.name = name;
        this.speed = speed;
        this.torque = torque;
        this.accel = accel;
        this.cycle = cycle;
    }

    // Getter & Setter (Giữ nguyên như cũ)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    public Double getTorque() { return torque; }
    public void setTorque(Double torque) { this.torque = torque; }
    public Double getAccel() { return accel; }
    public void setAccel(Double accel) { this.accel = accel; }
    public Double getCycle() { return cycle; }
    public void setCycle(Double cycle) { this.cycle = cycle; }
}
