package com.example.tepinhui.dto;

import java.util.List;

/**
 * 服务中心DTO
 */
public class ServiceCenterDTO {
    private List<FaqDTO> faqs;
    private List<ServiceCategoryDTO> categories;
    private ContactInfoDTO contactInfo;

    // Getter和Setter
    public List<FaqDTO> getFaqs() { return faqs; }
    public void setFaqs(List<FaqDTO> faqs) { this.faqs = faqs; }

    public List<ServiceCategoryDTO> getCategories() { return categories; }
    public void setCategories(List<ServiceCategoryDTO> categories) { this.categories = categories; }

    public ContactInfoDTO getContactInfo() { return contactInfo; }
    public void setContactInfo(ContactInfoDTO contactInfo) { this.contactInfo = contactInfo; }
}

class ContactInfoDTO {
    private String phone;
    private String onlineTime;
    private String email;
    private String wechat;
    private String qq;

    // Getter和Setter
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOnlineTime() { return onlineTime; }
    public void setOnlineTime(String onlineTime) { this.onlineTime = onlineTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWechat() { return wechat; }
    public void setWechat(String wechat) { this.wechat = wechat; }

    public String getQq() { return qq; }
    public void setQq(String qq) { this.qq = qq; }
}