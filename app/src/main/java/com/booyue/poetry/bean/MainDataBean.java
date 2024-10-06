package com.booyue.poetry.bean;

import java.util.List;

public class MainDataBean {


    private int code;
    private MainDataDTO data;
    private String info;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MainDataDTO getData() {
        return data;
    }

    public void setData(MainDataDTO data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class MainDataDTO {
        private int id;
        private List<ListDTO> themeList;
        private List<ListDTO> periodList;
        private List<DynastyListDTO> dynastyList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ListDTO> getThemeList() {
            return themeList;
        }

        public void setThemeList(List<ListDTO> themeList) {
            this.themeList = themeList;
        }

        public List<ListDTO> getPeriodList() {
            return periodList;
        }

        public void setPeriodList(List<ListDTO> periodList) {
            this.periodList = periodList;
        }

        public List<DynastyListDTO> getDynastyList() {
            return dynastyList;
        }

        public void setDynastyList(List<DynastyListDTO> dynastyList) {
            this.dynastyList = dynastyList;
        }


        public static class ListDTO {
            private List<MainDataDTO.ListDTO.DataDTO> data;
            private String title;

            public List<MainDataDTO.ListDTO.DataDTO> getData() {
                return data;
            }

            public void setData(List<MainDataDTO.ListDTO.DataDTO> data) {
                this.data = data;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class DataDTO {
                private int storyId;
                private String storyName;
                private String childrenName;
                private String readName;
                private String homeworkName;
                private String childrenSize;
                private int themeSort;
                private int readId;
                private String dynasty;
                private int periodSort;
                private String storyUrl;
                private String studyPhase;
                private String theme;
                private int dynastySort;
                private int id;
                private String readUrl;
                private String childrenUrl;
                private String storySize;
                private String author;
                private String studyContent;
                private String url;
                private String target;
                private int childrenId;
                private String imgUrl;
                private int homeworkId;
                private String name;
                private String homeworkSize;
                private String homeworkUrl;
                private int isDel;
                private String age;
                private String readSize;

                public String getReadName() {
                    return readName;
                }

                public void setReadName(String readName) {
                    this.readName = readName;
                }


                public String getChildrenName() {
                    return childrenName;
                }

                public void setChildrenName(String childrenName) {
                    this.childrenName = childrenName;
                }


                public String getStoryName() {
                    return storyName;
                }

                public void setStoryName(String storyName) {
                    this.storyName = storyName;
                }

                public String getHomeworkName() {
                    return homeworkName;
                }

                public void setHomeworkName(String homeworkName) {
                    this.homeworkName = homeworkName;
                }

                public int getStoryId() {
                    return storyId;
                }

                public void setStoryId(int storyId) {
                    this.storyId = storyId;
                }

                public String getChildrenSize() {
                    return childrenSize;
                }

                public void setChildrenSize(String childrenSize) {
                    this.childrenSize = childrenSize;
                }

                public int getThemeSort() {
                    return themeSort;
                }

                public void setThemeSort(int themeSort) {
                    this.themeSort = themeSort;
                }

                public int getReadId() {
                    return readId;
                }

                public void setReadId(int readId) {
                    this.readId = readId;
                }

                public String getDynasty() {
                    return dynasty;
                }

                public void setDynasty(String dynasty) {
                    this.dynasty = dynasty;
                }

                public int getPeriodSort() {
                    return periodSort;
                }

                public void setPeriodSort(int periodSort) {
                    this.periodSort = periodSort;
                }

                public String getStoryUrl() {
                    return storyUrl;
                }

                public void setStoryUrl(String storyUrl) {
                    this.storyUrl = storyUrl;
                }

                public String getStudyPhase() {
                    return studyPhase;
                }

                public void setStudyPhase(String studyPhase) {
                    this.studyPhase = studyPhase;
                }

                public String getTheme() {
                    return theme;
                }

                public void setTheme(String theme) {
                    this.theme = theme;
                }

                public int getDynastySort() {
                    return dynastySort;
                }

                public void setDynastySort(int dynastySort) {
                    this.dynastySort = dynastySort;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getReadUrl() {
                    return readUrl;
                }

                public void setReadUrl(String readUrl) {
                    this.readUrl = readUrl;
                }

                public String getChildrenUrl() {
                    return childrenUrl;
                }

                public void setChildrenUrl(String childrenUrl) {
                    this.childrenUrl = childrenUrl;
                }

                public String getStorySize() {
                    return storySize;
                }

                public void setStorySize(String storySize) {
                    this.storySize = storySize;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getStudyContent() {
                    return studyContent;
                }

                public void setStudyContent(String studyContent) {
                    this.studyContent = studyContent;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getTarget() {
                    return target;
                }

                public void setTarget(String target) {
                    this.target = target;
                }

                public int getChildrenId() {
                    return childrenId;
                }

                public void setChildrenId(int childrenId) {
                    this.childrenId = childrenId;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }

                public int getHomeworkId() {
                    return homeworkId;
                }

                public void setHomeworkId(int homeworkId) {
                    this.homeworkId = homeworkId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getHomeworkSize() {
                    return homeworkSize;
                }

                public void setHomeworkSize(String homeworkSize) {
                    this.homeworkSize = homeworkSize;
                }

                public String getHomeworkUrl() {
                    return homeworkUrl;
                }

                public void setHomeworkUrl(String homeworkUrl) {
                    this.homeworkUrl = homeworkUrl;
                }

                public int getIsDel() {
                    return isDel;
                }

                public void setIsDel(int isDel) {
                    this.isDel = isDel;
                }

                public String getAge() {
                    return age;
                }

                public void setAge(String age) {
                    this.age = age;
                }

                public String getReadSize() {
                    return readSize;
                }

                public void setReadSize(String readSize) {
                    this.readSize = readSize;
                }
            }
        }

        public static class DynastyListDTO {
            private List<MainDataDTO.DynastyListDTO.DataDTO> data;
            private String title;

            public List<MainDataDTO.DynastyListDTO.DataDTO> getData() {
                return data;
            }

            public void setData(List<MainDataDTO.DynastyListDTO.DataDTO> data) {
                this.data = data;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public static class DataDTO {
//                private int storyId;
//                private String childrenSize;
//                private int themeSort;
//                private int readId;
//                private String dynasty;
//                private int periodSort;
//                private String storyUrl;
//                private String studyPhase;
//                private String theme;
//                private int dynastySort;
//                private int id;
//                private String readUrl;
//                private String childrenUrl;
//                private String storySize;
//                private String author;
//                private String studyContent;
//                private String url;
//                private String target;
//                private int childrenId;
//                private String imgUrl;
//                private int homeworkId;
//                private String name;
//                private String homeworkSize;
//                private String homeworkUrl;
//                private int isDel;
//                private String age;
//                private String readSize;
//
//                public int getStoryId() {
//                    return storyId;
//                }
//
//                public void setStoryId(int storyId) {
//                    this.storyId = storyId;
//                }
//
//                public String getChildrenSize() {
//                    return childrenSize;
//                }
//
//                public void setChildrenSize(String childrenSize) {
//                    this.childrenSize = childrenSize;
//                }
//
//                public int getThemeSort() {
//                    return themeSort;
//                }
//
//                public void setThemeSort(int themeSort) {
//                    this.themeSort = themeSort;
//                }
//
//                public int getReadId() {
//                    return readId;
//                }
//
//                public void setReadId(int readId) {
//                    this.readId = readId;
//                }
//
//                public String getDynasty() {
//                    return dynasty;
//                }
//
//                public void setDynasty(String dynasty) {
//                    this.dynasty = dynasty;
//                }
//
//                public int getPeriodSort() {
//                    return periodSort;
//                }
//
//                public void setPeriodSort(int periodSort) {
//                    this.periodSort = periodSort;
//                }
//
//                public String getStoryUrl() {
//                    return storyUrl;
//                }
//
//                public void setStoryUrl(String storyUrl) {
//                    this.storyUrl = storyUrl;
//                }
//
//                public String getStudyPhase() {
//                    return studyPhase;
//                }
//
//                public void setStudyPhase(String studyPhase) {
//                    this.studyPhase = studyPhase;
//                }
//
//                public String getTheme() {
//                    return theme;
//                }
//
//                public void setTheme(String theme) {
//                    this.theme = theme;
//                }
//
//                public int getDynastySort() {
//                    return dynastySort;
//                }
//
//                public void setDynastySort(int dynastySort) {
//                    this.dynastySort = dynastySort;
//                }
//
//                public int getId() {
//                    return id;
//                }
//
//                public void setId(int id) {
//                    this.id = id;
//                }
//
//                public String getReadUrl() {
//                    return readUrl;
//                }
//
//                public void setReadUrl(String readUrl) {
//                    this.readUrl = readUrl;
//                }
//
//                public String getChildrenUrl() {
//                    return childrenUrl;
//                }
//
//                public void setChildrenUrl(String childrenUrl) {
//                    this.childrenUrl = childrenUrl;
//                }
//
//                public String getStorySize() {
//                    return storySize;
//                }
//
//                public void setStorySize(String storySize) {
//                    this.storySize = storySize;
//                }
//
//                public String getAuthor() {
//                    return author;
//                }
//
//                public void setAuthor(String author) {
//                    this.author = author;
//                }
//
//                public String getStudyContent() {
//                    return studyContent;
//                }
//
//                public void setStudyContent(String studyContent) {
//                    this.studyContent = studyContent;
//                }
//
//                public String getUrl() {
//                    return url;
//                }
//
//                public void setUrl(String url) {
//                    this.url = url;
//                }
//
//                public String getTarget() {
//                    return target;
//                }
//
//                public void setTarget(String target) {
//                    this.target = target;
//                }
//
//                public int getChildrenId() {
//                    return childrenId;
//                }
//
//                public void setChildrenId(int childrenId) {
//                    this.childrenId = childrenId;
//                }
//
//                public String getImgUrl() {
//                    return imgUrl;
//                }
//
//                public void setImgUrl(String imgUrl) {
//                    this.imgUrl = imgUrl;
//                }
//
//                public int getHomeworkId() {
//                    return homeworkId;
//                }
//
//                public void setHomeworkId(int homeworkId) {
//                    this.homeworkId = homeworkId;
//                }
//
//                public String getName() {
//                    return name;
//                }
//
//                public void setName(String name) {
//                    this.name = name;
//                }
//
//                public String getHomeworkSize() {
//                    return homeworkSize;
//                }
//
//                public void setHomeworkSize(String homeworkSize) {
//                    this.homeworkSize = homeworkSize;
//                }
//
//                public String getHomeworkUrl() {
//                    return homeworkUrl;
//                }
//
//                public void setHomeworkUrl(String homeworkUrl) {
//                    this.homeworkUrl = homeworkUrl;
//                }
//
//                public int getIsDel() {
//                    return isDel;
//                }
//
//                public void setIsDel(int isDel) {
//                    this.isDel = isDel;
//                }
//
//                public String getAge() {
//                    return age;
//                }
//
//                public void setAge(String age) {
//                    this.age = age;
//                }
//
//                public String getReadSize() {
//                    return readSize;
//                }
//
//                public void setReadSize(String readSize) {
//                    this.readSize = readSize;
//                }
            }
        }
    }
}