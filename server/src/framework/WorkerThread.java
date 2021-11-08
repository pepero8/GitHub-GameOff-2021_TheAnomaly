package framework;

abstract public class WorkerThread extends Thread {
	protected long MS_PER_UPDATE = 1000; // game progress time per update in millsecond

	protected MsgQueue msgque;

	protected boolean terminate;

	//constructor
	protected WorkerThread() {
	 	msgque = new MsgQueue();
	}

	@Override
	public void run() {
		ClientHandler nextInput;
		long previous = System.currentTimeMillis();
		long lag = 0;
		long current, elapsed;

		loop:
		while (!terminate) {
			try {
				current = System.currentTimeMillis();
				elapsed = current - previous;
				previous = current;
				lag += elapsed;

				// take and process all inputs from msgque
				int msgNum = msgque.size(); //메시지가 무한으로 계속 들어올 때 처리할 메시지 수를 제한하기 위해 만듦
				while (msgNum != 0) {
					nextInput = msgque.pollMsg();
					processInput(nextInput);
					if (terminate) break loop;
					msgNum--;
				}

				if (lag >= MS_PER_UPDATE) {
					while (lag >= MS_PER_UPDATE) { // MS_PER_UPDATE : 각 업데이트마다 진행시키는 게임 시간
						//System.out.println("[WorkThread]calling update");
						update(MS_PER_UPDATE);
						lag -= MS_PER_UPDATE;
					}

					//leftover lag?
					update(lag);
					lag = 0;
				}

				broadcast();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	MsgQueue getMsgQueue() {
		return msgque;
	}

	//should be called by only ClientHandler
	public abstract void bind(ClientHandler ch);

	//abstract protected void unbind(ClientHandler ch);

	abstract protected void processInput(ClientHandler msg);

	abstract protected void update(long progressTime);

	abstract protected void broadcast();
}