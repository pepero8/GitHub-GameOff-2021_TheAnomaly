package framework;
import java.util.LinkedList;
import java.util.Queue;

public class MsgQueue {
	private Queue<ClientHandler> msgque;

	public MsgQueue() {
		msgque = new LinkedList<ClientHandler>();
	}

	synchronized void offerMsg(ClientHandler msg) {
		msgque.offer(msg);
	}

	synchronized ClientHandler pollMsg() {
		return msgque.poll();
	}

	int size() {
		return msgque.size();
	}

	boolean isEmpty() {
		return msgque.isEmpty();
	}

	//may not need synchronization as this method is only called by worker threads for now
	public synchronized void removeMsg(ClientHandler ch) {
		msgque.remove(ch);
	}
}
