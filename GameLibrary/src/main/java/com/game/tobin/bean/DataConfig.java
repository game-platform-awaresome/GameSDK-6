package com.game.tobin.bean;

import java.util.List;

/**
 * Created by Tobin on 2017/8/31.
 */

public class DataConfig {

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String vstarGameCs;
        private String customerService;
        private String packs;
        private int isShowRecord;
        private String boostGPLink;
        private String facebook;
        private String config;
        private int forum;
        private String fb_copywriter;
        private String gw;
        private List<?> notice;

        public String getVstarGameCs() {
            return vstarGameCs;
        }

        public void setVstarGameCs(String vstarGameCs) {
            this.vstarGameCs = vstarGameCs;
        }

        public String getCustomerService() {
            return customerService;
        }

        public void setCustomerService(String customerService) {
            this.customerService = customerService;
        }

        public String getPacks() {
            return packs;
        }

        public void setPacks(String packs) {
            this.packs = packs;
        }

        public int getIsShowRecord() {
            return isShowRecord;
        }

        public void setIsShowRecord(int isShowRecord) {
            this.isShowRecord = isShowRecord;
        }

        public String getBoostGPLink() {
            return boostGPLink;
        }

        public void setBoostGPLink(String boostGPLink) {
            this.boostGPLink = boostGPLink;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        public int getForum() {
            return forum;
        }

        public void setForum(int forum) {
            this.forum = forum;
        }

        public String getFb_copywriter() {
            return fb_copywriter;
        }

        public void setFb_copywriter(String fb_copywriter) {
            this.fb_copywriter = fb_copywriter;
        }

        public String getGw() {
            return gw;
        }

        public void setGw(String gw) {
            this.gw = gw;
        }

        public List<?> getNotice() {
            return notice;
        }

        public void setNotice(List<?> notice) {
            this.notice = notice;
        }
    }
}
