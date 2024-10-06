package com.booyue.database.greendao.bean;

import com.booyue.database.DatabaseConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 下载任务的实体类
 * @author wangxinhua
 *
 */
@Entity(nameInDb = "downloadbean",generateConstructors = false)
public class DownloadBean implements Serializable{
	private static final long serialVersionUID = 8986524766018722786L;
	@Id(autoincrement = true)
	@Property(nameInDb = "id")
	public Long id;//数据表查询主键
	//	@Property(nameInDb = "picUrl")
//	public String picUrl;
	@Property(nameInDb = "url")
	public String url;
	@Property(nameInDb = "title")
	public String title;
	@Property(nameInDb = "isEnabled")
	public int isEnabled;//对应percent=（0,100），0表示false，1表示true
	@Property(nameInDb = "isFinished")
	public int isFinished;//对应percent=100,0表示false，1表示true
	@Property(nameInDb = "percent")
	public int percent;//表示当前下载任务的进度，需要持久化
	@Property(nameInDb = "completeSize")
	public long completeSize;//完成的大小
	@Property(nameInDb = "FileSize")
	public long FileSize;//文件的大小
	@Property(nameInDb = "guid")
	public long guid;//下载实体类唯一的标志id
	@Property(nameInDb = "state")
	public int state;//对应：暂停
	@Property(nameInDb = "type")
	public int type;//区分视频和音频

	@Property(nameInDb = "classname")
	public String classname;//属于的分类

	@Property(nameInDb = "localPath")
	public String localPath;//本地路径

	@Property(nameInDb = "timelength")
	public int timelength;//文件时长

	//科目，语文
	@Property(nameInDb = "subject")
	public String subject;

	//版名：人教版
	@Property(nameInDb = "version")
	public String version;

	//年级
	@Property(nameInDb = "grade")
	public String grade;

	//上下册
	@Property(nameInDb = "gradeAttr")
	public String gradeAttr;

	//第一单元
	@Property(nameInDb = "unit")
	public String unit;
	//封面图
	@Property(nameInDb = "coverImage")
	public String coverImage;
	//多选
	@Property(nameInDb = "isChack")
	public Boolean isChack;
	//分组名称
	@Property(nameInDb = "groupName")
	public String groupName;
	//分组id
	@Property(nameInDb = "groupId")
	public String groupId;


	/**
	 * 默认的构造方法，必须有
	 */
	public DownloadBean(){

	}
	/**
	 * 新建任务，没有状态和进度，默认isEnabled,isFinished都是false
	 */
	public DownloadBean(long guid,String url,String title,String classname,int type,int timelength){
		this.timelength = timelength;
		this.guid = guid;
		this.url = url;
		this.title = title;
		this.classname = classname;
		this.type = type;
		this.percent = 0;
		this.completeSize = 0;
		this.isEnabled = DatabaseConfig.FALSE;
		this.isFinished = DatabaseConfig.FALSE;
		this.state = DatabaseConfig.STATE_TASK_WAIT;

		//等待处理
		this.state = DatabaseConfig.STATE_TASK_WAIT;
	}
	/**
	 * 从本地持久化处新建对象，包含状态和进度
	 * @param guid
	 * @param url
	 * @param title
	 * @param state 0为初始状态，1下载中，2暂停
	 * @param percent
	 */
	public DownloadBean(long guid,String url,String title,int state,int percent,int timelength){
		super();
		this.guid = guid;
		this.url = url;
		this.title = title;
		this.timelength = timelength;
		this.percent = percent;
		this.state = state;
	}

	public int State() {
		return state;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIsEnabled() {
		return this.isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public int getIsFinished() {
		return this.isFinished;
	}
	public void setIsFinished(int isFinished) {
		this.isFinished = isFinished;
	}
	public int getPercent() {
		return this.percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	public long getCompleteSize() {
		return this.completeSize;
	}
	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}
	public long getFileSize() {
		return this.FileSize;
	}
	public void setFileSize(long FileSize) {
		this.FileSize = FileSize;
	}
	public long getGuid() {
		return this.guid;
	}
	public void setGuid(long guid) {
		this.guid = guid;
	}
	public int getState() {
		return this.state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getClassname() {
		return this.classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getLocalPath() {
		return this.localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public int getTimelength() {
		return this.timelength;
	}
	public void setTimelength(int timelength) {
		this.timelength = timelength;
	}
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getGrade() {
		return this.grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGradAttr() {
		return this.gradeAttr;
	}
	public void setGradAttr(String gradAttr) {
		this.gradeAttr = gradAttr;
	}
	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCoverImage() {
		return this.coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public Boolean getIsChack() {
		return this.isChack;
	}
	public void setIsChack(Boolean isChack) {
		this.isChack = isChack;
	}
	public String getGradeAttr() {
		return this.gradeAttr;
	}
	public void setGradeAttr(String gradeAttr) {
		this.gradeAttr = gradeAttr;
	}

}
