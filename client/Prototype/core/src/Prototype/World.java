package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class World implements Disposable {
	private static final int MAIN_AREA_NUM = 0;

	public Player robot;
	public Player player1;
	//public Player player2;
	//public Player player3;
	//public Player player4;
	//public Array<Player> activePlayers; // array containing alive players(not including robot)

	public Player[] players;

	public Area mainArea;
	//public Area area2;
	//public Area area3;

	public Area[] areas;

	public CardKeyObject cardKey;

	public long remainTime;

	//constructor
	public World() {
		players = new Player[2];
		areas = new Area[1];
		
		//create areas
		mainArea = new Area(MAIN_AREA_NUM, 4) {
			@Override
			public Area determineArea(float x, float y) {
				//entry line to area2
				// if (x == Prototype.MAP_WIDTH*3/8 - Prototype.CHAR_WIDTH && (y >= Prototype.MAP_HEIGHT*3/8 && y <= Prototype.MAP_HEIGHT*5/8 - Prototype.CHAR_HEIGHT)) {
				// 	return area2;
				// }
				// else
				// 	return this;
				return this;
			}
		};
		//add objects to the area
		mainArea.addObject(new BoxObject(572, 128, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "main area box1"));
		mainArea.addObject(new DoorObject(0, 668, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test door"));
		mainArea.addObject(new GateObject(888, 0, GateObject.GATEOBJECT_HEIGHT, GateObject.GATEOBJECT_WIDTH, "test gate"));

		mainArea.setBounds(0, 0, 1144, 1336);
		mainArea.setName("main zone");

		areas[MAIN_AREA_NUM] = mainArea;
		// area1.setBounds(0, 0, Prototype.MAP_WIDTH*3/8-Prototype.CHAR_WIDTH, Prototype.MAP_HEIGHT-Prototype.CHAR_HEIGHT);
		// area1.setName("area1");
		// area2 = new Area() {
		// 	@Override
		// 	public Area determineArea(float x, float y) {
		// 		//entry line to area1
		// 		if (x == Prototype.MAP_WIDTH*3/8 - Prototype.CHAR_WIDTH && (y >= Prototype.MAP_HEIGHT*3/8 && y <= Prototype.MAP_HEIGHT*5/8 - Prototype.CHAR_HEIGHT)) {
		// 			return area1;
		// 		}
		// 		//entry line to area3
		// 		else if (x == Prototype.MAP_WIDTH*5/8 && (y >= Prototype.MAP_HEIGHT*3/8 && y <= Prototype.MAP_HEIGHT*5/8 - Prototype.CHAR_HEIGHT)) {
		// 			return area3;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };
		// area2.setBounds(Prototype.MAP_WIDTH*3/8-Prototype.CHAR_WIDTH, Prototype.MAP_HEIGHT*3/8, Prototype.MAP_WIDTH/4+Prototype.CHAR_WIDTH, Prototype.MAP_HEIGHT/4-Prototype.CHAR_HEIGHT);
		// area2.setName("area2");
		// area3 = new Area() {
		// 	@Override
		// 	public Area determineArea(float x, float y) {
		// 		//entry line to area2
		// 		if (x == Prototype.MAP_WIDTH*5/8 && (y >= Prototype.MAP_HEIGHT*3/8 && y <= Prototype.MAP_HEIGHT*5/8 - Prototype.CHAR_HEIGHT)) {
		// 			return area2;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };
		// area3.setBounds(Prototype.MAP_WIDTH*5/8, 0, Prototype.MAP_WIDTH*3/8-Prototype.CHAR_WIDTH, Prototype.MAP_HEIGHT-Prototype.CHAR_HEIGHT);
		// area3.setName("area3");
		
		//create characters
		robot = new Player();
		// robot.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		//robot.setPos(0, 0); // sets the robot's initial position to the middle
		robot.setCurrentArea(mainArea);

		player1 = new Player();
		//player1.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		//player1.setPos(50, 200); //sets the player1's position on the upper left quarter of the map
		player1.setCurrentArea(mainArea);

		// player2 = new Player();
		// //player2.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		// player2.setPos(Prototype.MAP_WIDTH/2 - Prototype.CHAR_WIDTH/2, Prototype.MAP_HEIGHT/2 - Prototype.CHAR_HEIGHT/2); //sets the player2's position on the bottom left quarter of the map
		// player2.setCurrentArea(area2);

		// player3 = new Player();
		// //player3.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		// player3.setPos(player2.getX()+70, Prototype.MAP_HEIGHT/2 - Prototype.CHAR_HEIGHT/2); //sets the player3's position on the upper right quarter of the map
		// player3.setCurrentArea(area2);
		
		// player4 = new Player();
		// //player4.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		// player4.setPos(Prototype.MAP_WIDTH*5/8+100, Prototype.MAP_HEIGHT/2 - Prototype.CHAR_HEIGHT/2); //sets the player4's position on the bottom right quarter of the map
		// player4.setCurrentArea(area3);
		
		//activePlayers = new Array<Player>();
		//activePlayers.add(player1);
		//activePlayers.add(player1, player2, player3, player4);

		players[0] = robot;
		players[1] = player1;

		//create card key object
		cardKey = new CardKeyObject(this, null, -1, -1, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key");
		//cardKey = new CardKeyObject(this, mainArea, 872, 240, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key");
		//mainArea.addObject(cardKey);
	}

	public void setRemainTime(long remain) {
		remainTime = remain;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		robot.dispose();
		player1.dispose();
		// player2.dispose();
		// player3.dispose();
		// player4.dispose();
		//area1.dispose();
		//area2.dispose();
		//area3.dispose();

		Gdx.app.log("World", "disposed");
	}
}
