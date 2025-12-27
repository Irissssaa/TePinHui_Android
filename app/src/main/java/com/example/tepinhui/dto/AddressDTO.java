package com.example.tepinhui.dto;

/**
 * 收货地址DTO
 */
public class AddressDTO {
    private Integer id;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Boolean isDefault;
    private String postCode;

    // Getter和Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }

    // 获取完整地址
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (province != null) sb.append(province);
        if (city != null) sb.append(city);
        if (district != null) sb.append(district);
        if (detail != null) sb.append(detail);
        return sb.toString();
    }
}