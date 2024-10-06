package com.booyue.poetry.bean;

import com.booyue.base.bean.BaseBean2;

import java.util.List;

/**
 * @author: wangxinhua
 * @date: 2020/6/15 15:02
 * @description :
 */
public class SearchConditionBean extends BaseBean2 {
    /**
     * data : {"gradeList":["二年级","三年级","四年级"],"subjectList":["数学","语文","英语"],"versionList":["人教版","苏教版","鲁教版"]}
     */
    public DataBean data;

    public static class DataBean {
        public List<String> gradeList;
        public List<String> subjectList;
        public List<String> versionList;
    }
}
