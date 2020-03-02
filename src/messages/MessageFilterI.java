package messages;

@FunctionalInterface
public interface MessageFilterI {
	boolean filter(MessageI m);
}
