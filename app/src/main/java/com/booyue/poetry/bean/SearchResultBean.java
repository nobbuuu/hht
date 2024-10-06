package com.booyue.poetry.bean;

import com.booyue.base.bean.BaseBean2;
import com.booyue.media.bean.VideoBean;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/15 15:15
 * @description : 搜索结果实体类
 */
public class SearchResultBean extends BaseBean2 {
    public List<BookBean> data;

    public static class BookBean {
        public String coverImage;//书本图片
        public String grade;//年级
        public String gradeAtt;//上 下
        public String subject;//科目，语文，数学
        public String version;//人教版，
        public List<BookUnitBean> resList;

        public static class BookUnitBean {
            public String unit;//第一单元
            public List<BookUnitVideoBean> videoList;//视频实体类类表

            public static class BookUnitVideoBean extends VideoBean {
                public String createTime;//创建时间
                public String fileSize;//文件大小 M
                public int id;
                public int isDel;
                public String remarks;
                public String updateTime;


                public String unit;//多添加，下载到数据库需要用到
                public String grade;//年级
                public String gradeAtt;//上 下
                public String subject;//科目，语文，数学
                public String version;//人教版，
                public String coverImage;//封面图，
            }
        }
    }
}
