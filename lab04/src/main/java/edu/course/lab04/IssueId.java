package edu.course.lab04;

import java.util.Objects;

/**
 * Неизменяемый составной ключ задачи: код проекта и номер задачи.
 */
public final class IssueId {

    private final String projectCode;
    private final int issueNumber;

    public IssueId(String projectCode, int issueNumber) {
        if (projectCode == null || projectCode.isBlank()) {
            throw new IllegalArgumentException("Project code must not be null or blank");
        }
        if (issueNumber <= 0) {
            throw new IllegalArgumentException("Issue number must be positive: " + issueNumber);
        }
        this.projectCode = projectCode.trim().toUpperCase();
        this.issueNumber = issueNumber;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueId issueId = (IssueId) o;
        return issueNumber == issueId.issueNumber && Objects.equals(projectCode, issueId.projectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode, issueNumber);
    }

    @Override
    public String toString() {
        return projectCode + "-" + issueNumber;
    }
}
