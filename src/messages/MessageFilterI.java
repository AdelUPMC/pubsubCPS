package messages;

@FunctionalInterface
public interface MessageFilterI {
	public boolean filter(MessageI m);
}
