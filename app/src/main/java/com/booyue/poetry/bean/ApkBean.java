package com.booyue.poetry.bean;

import java.util.List;

public class ApkBean {


    private ContentDTO content;
    private String msg;
    private String ret;

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class ContentDTO {
        private List<ListDTO> list;

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public static class ListDTO {
            private String apk;
            private String packageName;
            private int size;
            private int update;
            private Long versionCode;

            public String getApk() {
                return apk;
            }

            public void setApk(String apk) {
                this.apk = apk;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getUpdate() {
                return update;
            }

            public void setUpdate(int update) {
                this.update = update;
            }

            public Long getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(Long versionCode) {
                this.versionCode = versionCode;
            }
        }
    }
}
