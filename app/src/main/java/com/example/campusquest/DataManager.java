package com.example.campusquest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.campusquest.CampusQuestDatabaseContract.QuestsInfoEntry;

/**
 * Singleton service for accessing and managing app data that is moved around
 * the application (e.g. username.)
 */

public class DataManager {

    private static DataManager sInstance;
    private static String sUsername;
    private List<QuestInfo> mQuests = new ArrayList<>();
    private int mPicId = R.drawable.gunslinger;
    private String mClassName = "Gunslinger";

    /** Use this static method to access instance to ensure only one instance is created and used.
     */
    public static DataManager getInstance() {

        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    /** Retrieve current user name if available or pass back
     * test name if in test mode.
     */
    public String getCurrentUserName() {
        if (sUsername != null) {
            return sUsername;
        } else {
            return "Tess McTestalot";
        }

    }

    public int getCurrentProfilePic() {
        return mPicId;
    }

    public void setCurrentProfilePic(int newId) {
        mPicId = newId;
    }

    public void setCurrentClass(String name ) {
        mClassName = name;
    }

    public String getCurrentClass() {
        return mClassName;
    }

    public void setCurrentUserName(String name) {
        sUsername = name;
    }

    public List<QuestInfo> getQuests() {
        return mQuests;
    }

    /**
     * Returns a list of all available quest types from quest table.
     * @param dbHelper
     */

    public static void loadQuests(CampusQuestOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] questColumns = {
                QuestsInfoEntry.COLUMN_QUEST_NAME,
                QuestsInfoEntry.COLUMN_TOTAL_STAGES,
                QuestsInfoEntry.COLUMN_QUEST_ID};
        final Cursor questCursor = db.query(QuestsInfoEntry.TABLE_NAME, questColumns,
                null, null, null, null, null);
        loadQuestsFromDatabase(questCursor);
    }

    /**
     * Moves through all rows in quest table, creates a list of quest into objects and appends to
     * an array.
     * @param cursor
     */

    public static void loadQuestsFromDatabase(Cursor cursor) {
        int questNamePos = cursor.getColumnIndex(QuestsInfoEntry.COLUMN_QUEST_NAME);
        int totalStagePos = cursor.getColumnIndex(QuestsInfoEntry.COLUMN_TOTAL_STAGES);
        int questIdPos = cursor.getColumnIndex(QuestsInfoEntry.COLUMN_QUEST_ID);

        DataManager dm = getInstance();

        while(cursor.moveToNext()) {
            String questId = cursor.getString(questIdPos);
            String questName = cursor.getString(questNamePos);
            int totalStages = cursor.getInt(totalStagePos);

            QuestInfo quest = new QuestInfo(questId, questName,totalStages);

            dm.mQuests.add(quest);

        }
    }
}
