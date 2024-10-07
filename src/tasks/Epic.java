package tasks;

import managers.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {
    private ArrayList<Integer> subtasksOfSpecificEpic;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtasksOfSpecificEpic = new ArrayList<>();
        setType(Types.EPIC);
    }

    public void findStartTime(ArrayList<Subtask> subtasksOfEpic) {
        if (subtasksOfSpecificEpic.isEmpty()) {
            setStartTime(null);
        } else {
            List<Subtask> filteredListOfSubtasksByStart = subtasksOfEpic.stream().filter(subtask -> subtask.getStartTime() != null).collect(Collectors.toList());
            if (filteredListOfSubtasksByStart.isEmpty()) {
                setStartTime(null);
            } else {
                filteredListOfSubtasksByStart.sort((subtask1, subtask2) -> {
                    if (subtask1.getStartTime().isBefore(subtask2.getStartTime())) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                setStartTime(filteredListOfSubtasksByStart.get(0).getStartTime());
            }

        }
    }

    public void findDuration(ArrayList<Subtask> subtasksOfEpic) {
        long minutes = 0;
        if (!subtasksOfSpecificEpic.isEmpty()) {
            for (Subtask subtask : subtasksOfEpic) {
                minutes += subtask.getDuration().toMinutes();
            }

        }
        setDuration(Duration.ofMinutes(minutes));

    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void findEndTime(ArrayList<Subtask> subtasksOfEpic) {
        if (subtasksOfSpecificEpic.isEmpty()) {
            setEndTime(null);
        } else {
            List<Subtask> filteredListOfSubtasksByStart = subtasksOfEpic.stream().filter(subtask -> subtask.getStartTime() != null).collect(Collectors.toList());
            if (filteredListOfSubtasksByStart.isEmpty()) {
                setEndTime(null);
            } else {
                filteredListOfSubtasksByStart.sort((subtask1, subtask2) -> {
                    if (subtask2.getStartTime().isBefore(subtask1.getStartTime())) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                setEndTime(filteredListOfSubtasksByStart.get(0).getEndTime());

            }

        }

    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtasksOfSpecificEpic() {
        return subtasksOfSpecificEpic;
    }

    public void setSubtasksOfSpecificEpic(ArrayList<Integer> subtasksOfSpecificEpic) {
        this.subtasksOfSpecificEpic = subtasksOfSpecificEpic;
    }

    @Override
    public String toString() {
        return "TaskKindObjects.Epic{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }
}
