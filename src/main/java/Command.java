@FunctionalInterface
public interface Command {
    public boolean execute(String... args);
}
