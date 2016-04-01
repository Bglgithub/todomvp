package com.example.mrtan.mvptest1.data;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Objects;

import java.util.UUID;

public final class Task {

    private final String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDescription;

    private final boolean mCompleted;

    /**
     * use this constructor to create a new active Task
     *
     * @param title       名称  名称
     * @param description 描述
     */
    public Task(@Nullable String title, @Nullable String description) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    /**
     * Use  this constructor to create an active Task if the Task already has id(copy of another Task)
     *
     * @param title       名称
     * @param description 描述
     * @param id          id
     */
    public Task(@Nullable String title, @Nullable String description, String id) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = false;
    }

    /**
     * Use this constructor to create a new completed Task
     *
     * @param title       名称
     * @param description 描述
     * @param completed   是否已完成
     */
    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id(copy of other Task)
     *
     * @param title       名称
     * @param description 描述
     * @param id          id
     * @param completed   是否已完成
     */
    public Task(@Nullable String title, @Nullable String description, String id, boolean completed) {
        mId = id;
        mCompleted = completed;
        mTitle = title;
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getTitleForList() {
        if (TextUtils.isEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
