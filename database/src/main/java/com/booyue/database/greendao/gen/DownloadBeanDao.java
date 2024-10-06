package com.booyue.database.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.booyue.database.greendao.bean.DownloadBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "downloadbean".
*/
public class DownloadBeanDao extends AbstractDao<DownloadBean, Long> {

    public static final String TABLENAME = "downloadbean";

    /**
     * Properties of entity DownloadBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Url = new Property(1, String.class, "url", false, "url");
        public final static Property Title = new Property(2, String.class, "title", false, "title");
        public final static Property IsEnabled = new Property(3, int.class, "isEnabled", false, "isEnabled");
        public final static Property IsFinished = new Property(4, int.class, "isFinished", false, "isFinished");
        public final static Property Percent = new Property(5, int.class, "percent", false, "percent");
        public final static Property CompleteSize = new Property(6, long.class, "completeSize", false, "completeSize");
        public final static Property FileSize = new Property(7, long.class, "FileSize", false, "FileSize");
        public final static Property Guid = new Property(8, long.class, "guid", false, "guid");
        public final static Property State = new Property(9, int.class, "state", false, "state");
        public final static Property Type = new Property(10, int.class, "type", false, "type");
        public final static Property Classname = new Property(11, String.class, "classname", false, "classname");
        public final static Property LocalPath = new Property(12, String.class, "localPath", false, "localPath");
        public final static Property Timelength = new Property(13, int.class, "timelength", false, "timelength");
        public final static Property Subject = new Property(14, String.class, "subject", false, "subject");
        public final static Property Version = new Property(15, String.class, "version", false, "version");
        public final static Property Grade = new Property(16, String.class, "grade", false, "grade");
        public final static Property GradeAttr = new Property(17, String.class, "gradeAttr", false, "gradeAttr");
        public final static Property Unit = new Property(18, String.class, "unit", false, "unit");
        public final static Property CoverImage = new Property(19, String.class, "coverImage", false, "coverImage");
        public final static Property IsChack = new Property(20, Boolean.class, "isChack", false, "isChack");
        public final static Property GroupName = new Property(21, String.class, "groupName", false, "groupName");
        public final static Property GroupId = new Property(22, String.class, "groupId", false, "groupId");
    }


    public DownloadBeanDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"downloadbean\" (" + //
                "\"id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"url\" TEXT," + // 1: url
                "\"title\" TEXT," + // 2: title
                "\"isEnabled\" INTEGER NOT NULL ," + // 3: isEnabled
                "\"isFinished\" INTEGER NOT NULL ," + // 4: isFinished
                "\"percent\" INTEGER NOT NULL ," + // 5: percent
                "\"completeSize\" INTEGER NOT NULL ," + // 6: completeSize
                "\"FileSize\" INTEGER NOT NULL ," + // 7: FileSize
                "\"guid\" INTEGER NOT NULL ," + // 8: guid
                "\"state\" INTEGER NOT NULL ," + // 9: state
                "\"type\" INTEGER NOT NULL ," + // 10: type
                "\"classname\" TEXT," + // 11: classname
                "\"localPath\" TEXT," + // 12: localPath
                "\"timelength\" INTEGER NOT NULL ," + // 13: timelength
                "\"subject\" TEXT," + // 14: subject
                "\"version\" TEXT," + // 15: version
                "\"grade\" TEXT," + // 16: grade
                "\"gradeAttr\" TEXT," + // 17: gradeAttr
                "\"unit\" TEXT," + // 18: unit
                "\"coverImage\" TEXT," + // 19: coverImage
                "\"isChack\" INTEGER," + // 20: isChack
                "\"groupName\" TEXT," + // 21: groupName
                "\"groupId\" TEXT);"); // 22: groupId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"downloadbean\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
        stmt.bindLong(4, entity.getIsEnabled());
        stmt.bindLong(5, entity.getIsFinished());
        stmt.bindLong(6, entity.getPercent());
        stmt.bindLong(7, entity.getCompleteSize());
        stmt.bindLong(8, entity.getFileSize());
        stmt.bindLong(9, entity.getGuid());
        stmt.bindLong(10, entity.getState());
        stmt.bindLong(11, entity.getType());
 
        String classname = entity.getClassname();
        if (classname != null) {
            stmt.bindString(12, classname);
        }
 
        String localPath = entity.getLocalPath();
        if (localPath != null) {
            stmt.bindString(13, localPath);
        }
        stmt.bindLong(14, entity.getTimelength());
 
        String subject = entity.getSubject();
        if (subject != null) {
            stmt.bindString(15, subject);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(16, version);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(17, grade);
        }
 
        String gradeAttr = entity.getGradeAttr();
        if (gradeAttr != null) {
            stmt.bindString(18, gradeAttr);
        }
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(19, unit);
        }
 
        String coverImage = entity.getCoverImage();
        if (coverImage != null) {
            stmt.bindString(20, coverImage);
        }
 
        Boolean isChack = entity.getIsChack();
        if (isChack != null) {
            stmt.bindLong(21, isChack ? 1L: 0L);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(22, groupName);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(23, groupId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
        stmt.bindLong(4, entity.getIsEnabled());
        stmt.bindLong(5, entity.getIsFinished());
        stmt.bindLong(6, entity.getPercent());
        stmt.bindLong(7, entity.getCompleteSize());
        stmt.bindLong(8, entity.getFileSize());
        stmt.bindLong(9, entity.getGuid());
        stmt.bindLong(10, entity.getState());
        stmt.bindLong(11, entity.getType());
 
        String classname = entity.getClassname();
        if (classname != null) {
            stmt.bindString(12, classname);
        }
 
        String localPath = entity.getLocalPath();
        if (localPath != null) {
            stmt.bindString(13, localPath);
        }
        stmt.bindLong(14, entity.getTimelength());
 
        String subject = entity.getSubject();
        if (subject != null) {
            stmt.bindString(15, subject);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(16, version);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(17, grade);
        }
 
        String gradeAttr = entity.getGradeAttr();
        if (gradeAttr != null) {
            stmt.bindString(18, gradeAttr);
        }
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(19, unit);
        }
 
        String coverImage = entity.getCoverImage();
        if (coverImage != null) {
            stmt.bindString(20, coverImage);
        }
 
        Boolean isChack = entity.getIsChack();
        if (isChack != null) {
            stmt.bindLong(21, isChack ? 1L: 0L);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(22, groupName);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(23, groupId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DownloadBean readEntity(Cursor cursor, int offset) {
        DownloadBean entity = new DownloadBean();
        readEntity(cursor, entity, offset);
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsEnabled(cursor.getInt(offset + 3));
        entity.setIsFinished(cursor.getInt(offset + 4));
        entity.setPercent(cursor.getInt(offset + 5));
        entity.setCompleteSize(cursor.getLong(offset + 6));
        entity.setFileSize(cursor.getLong(offset + 7));
        entity.setGuid(cursor.getLong(offset + 8));
        entity.setState(cursor.getInt(offset + 9));
        entity.setType(cursor.getInt(offset + 10));
        entity.setClassname(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLocalPath(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setTimelength(cursor.getInt(offset + 13));
        entity.setSubject(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setVersion(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setGrade(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setGradeAttr(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setUnit(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCoverImage(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setIsChack(cursor.isNull(offset + 20) ? null : cursor.getShort(offset + 20) != 0);
        entity.setGroupName(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setGroupId(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DownloadBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DownloadBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
