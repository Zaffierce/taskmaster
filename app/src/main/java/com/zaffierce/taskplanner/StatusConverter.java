package com.zaffierce.taskplanner;

import androidx.room.TypeConverter;

import static com.zaffierce.taskplanner.Task.TaskState.*;

public class StatusConverter {

    //Thank you all mighty Stack Overflow && Michelle
    //▄█▀█● ▄█▀█● ▄█▀█●
    //https://stackoverflow.com/questions/44498616/android-architecture-components-using-enums

    @TypeConverter
    public static Task.TaskState toStatus(int status) {
        if (status == NEW.getCode()) {
            return NEW;
        } else if (status == ASSIGNED.getCode()) {
            return ASSIGNED;
        } else if (status == IN_PROGRESS.getCode()) {
            return IN_PROGRESS;
        } else if (status == COMPLETE.getCode()) {
            return COMPLETE;
        } else {
            throw new IllegalArgumentException("Error finding status");
        }
    }

    @TypeConverter
    public static int toInt(Task.TaskState status) {
        return status.getCode();
    }
}
