public class Subtask extends Task{

 private int idOfEpic;

    Subtask(String name, String description, int idOfEpic){
        super(name, description, Status.NEW);
        this.idOfEpic = idOfEpic;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
