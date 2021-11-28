package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class World extends Actor implements Disposable {
	/*adding new area
	  1. add AREA_NAME_NUM
	  2. add area global variable
	  3. increase size of areas by 1
	  4. instantiate the area & add/update objects
	  5. insert area into areas
	  6. update determineArea() of all areas
	  7. if any determineArea() changed, apply to the server
	*/
	private static final int NUM_AREAS = 14;

	private static final int MAIN_AREA_NUM = 0;
	private static final int PATHTOENTRANCE_AREA_NUM = 1;
	private static final int WESTPASSAGE_AREA_NUM = 2;
	private static final int WESTHALLWAY_AREA_NUM = 3;
	private static final int SOUTHPASSAGE_AREA_NUM = 4;
	private static final int SOUTHHALLWAY_AREA_NUM = 5;
	private static final int TESTROOM_AREA_NUM = 6;
	private static final int DEVELOPMENTROOM_AREA_NUM = 7;
	private static final int DIRECTOROFFICE_AREA_NUM = 8;
	private static final int SERVERROOM_AREA_NUM = 9;
	private static final int OFFICE1_AREA_NUM = 10;
	private static final int OFFICE2_AREA_NUM = 11;
	private static final int OFFICE3_AREA_NUM = 12;
	private static final int PATHTOEXIT_AREA_NUM = 13;

	Assets assets;

	public Player robot;
	public Player player1;
	public Player player2;
	//public Player player3;
	//public Player player4;
	//public Array<Player> activePlayers; // array containing alive players(not including robot)

	public Player[] players;

	public Area mainArea;
	public Area pathToEntranceArea;
	public Area westPassageArea;
	public Area westHallwayArea;
	public Area southPassageArea;
	public Area southHallwayArea;
	public Area testRoomArea;
	public Area developmentRoomArea;
	public Area directorOfficeArea;
	public Area serverRoomArea;
	public Area office1Area;
	public Area office2Area;
	public Area office3Area;
	public Area pathToExitArea;
	//public Area area3;

	public Area[] areas;

	public CardKeyObject cardKey;

	public long remainTime = 300000;

	//constructor
	public World() {
		assets = new Assets();

		players = new Player[Prototype.NUM_PLAYERS];
		areas = new Area[NUM_AREAS];

		//create shared doors/gates/walls
		GateObject mainGate1 = new GateObject(784+0, 1088+1536-5, GateObject.GATEOBJECT_WIDTH, GateObject.GATEOBJECT_HEIGHT+5, 0, 5, "entrance gate1", assets.gateAnimation);
		GateObject mainGate2 = new GateObject(784+256*3, 1088+1536-5, GateObject.GATEOBJECT_WIDTH, GateObject.GATEOBJECT_HEIGHT+5, 0, 5, "entrance gate2", assets.gateAnimation);
		WallObject entranceWall = new WallObject(784 + 256, 1088 + 1536 - 5, 512, 256 + 5);

		DoorObject developmentRoomEntrance = new DoorObject(2048, 640, 16, 128, "development room entrance", assets.doorAnimationRotated);
		WallObject developmentRoomWall1 = new WallObject(2048 + 1, 768, 16, 64);
		WallObject developmentRoomWall2 = new WallObject(2048 + 1, 576, 16, 64);

		DoorObject directorOfficeDoor = new DoorObject(256+16+64, 512+1600, 128, 64, "director's office door", assets.doorAnimation);
		WallObject directorOfficeWall1 = new WallObject(256+16, 512+1600, 64, 64);
		WallObject directorOfficeWall2 = new WallObject(256+16+256-64, 512+1600, 64, 64);
		
		DoorObject testRoomDoor = new DoorObject(1168, 512, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test room entrance", assets.doorAnimation);
		WallObject testRoomWall = new WallObject(1168+128, 512, 512-128, 64+1);
		
		DoorObject serverRoomDoor = new DoorObject(256+16+64, 512, 128, 64, "server room door", assets.doorAnimation);
		WallObject serverRoomWall1 = new WallObject(256+16, 512, 64, 64);
		WallObject serverRoomWall2 = new WallObject(256+16+256-64, 512, 64, 64);

		DoorObject office1Door = new DoorObject(256, 1088+512, 16, 128, "office 1 door", assets.doorAnimationRotated);
		WallObject office1Wall = new WallObject(256, 1088+512+128, 16,128);
		
		DoorObject office2Door = new DoorObject(256, 1088 + 128, 16, 128, "office 2 door", assets.doorAnimationRotated);
		WallObject office2Wall = new WallObject(256, 1088 + 128 + 128, 16, 128);
		
		DoorObject office3Door = new DoorObject(256, 1088 - 256, 16, 128, "office 3 door", assets.doorAnimationRotated);
		WallObject office3Wall = new WallObject(256, 1088 - 256 + 128, 16, 128);
		
		//create areas
		mainArea = new Area(MAIN_AREA_NUM, 18, 16, 16, assets.mainAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				//System.out.println("Main area determine");
				//float originX = getX();
				//float originY = getY();

				//entry line to pathToEntranceArea
				if (y >= getY() + 1536) {
					return pathToEntranceArea;
				}
				// else if (x <= getX() - 16) {
				else if (x <= getX() && y >= 1088 + 512 && y <= 1088+512+256) {
					return westPassageArea;
				}
				// else if (y <= getY() - 16) {
				else if (y <= getY() && x >= 1168 && x <= 1168+256) {
					return southPassageArea;
				}
				else
					return this;
			}
		};
		mainArea.setBounds(784, 1088, 1024, 1536);
		mainArea.setName("main zone");
		//add objects to the area
		// the object which two areas are sharing must be added at the same index of
		// each object array(object's num is determined by its index number).
		mainArea.addObject(entranceWall);
		mainArea.addObject(mainGate1);
		mainArea.addObject(mainGate2);
		mainArea.addObject(new WallObject(784+256, 1088+1024, 16, 256));
		mainArea.addObject(new WallObject(784+256+16, 1088+1024+64*3, 512-32, 64));
		mainArea.addObject(new WallObject(784+256*3-16, 1088+1024, 16, 256));
		mainArea.addObject(new WallObject(784+256, 1088+512+128, 16, 256));
		mainArea.addObject(new WallObject(784+256+16, 1088+512+128, 512-32, 64));
		mainArea.addObject(new WallObject(784+256*3-16, 1088+512+128, 16, 256));
		mainArea.addObject(new WallObject(784+256, 1088+256, 512, 384));
		mainArea.addObject(new WallObject(1056+192, 1792+176, 96, 160)); //table
		mainArea.addObject(new WallObject(1056 + 192+32, 1792 + 176+160, 32, 32)); //chair1
		//mainArea.addObject(new WallObject(1056 + 192+96, 1792 + 176+160-32, 32, 32)); //chair2
		//mainArea.addObject(new WallObject(1056 + 192+96, 1792 + 176+160-96, 32, 32)); // chair3
		mainArea.addObject(new WallObject(1056 + 192+96, 1792 + 176, 32, 160)); // chair4
		mainArea.addObject(new WallObject(1056 + 192-32, 1792 + 176, 32, 160)); // chair5
		//mainArea.addObject(new WallObject(1056 + 192-32, 1792 + 176+64, 32, 32)); // chair6
		//mainArea.addObject(new WallObject(1056 + 192-32, 1792 + 176 + 160 - 32, 32, 32)); // chair7
		mainArea.addObject(new BoxObject(1056, 1792, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box1", assets.boxTexture));
		mainArea.addObject(new BoxObject(1056+480-32, 1792+512-32, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box2", assets.boxTexture));
		mainArea.addObject(new BoxObject(1056+480-32, 1792+160, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box3", assets.boxTexture));
		//mainArea.addObject(new DoorObject(0, 668, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test door", assets.doorAnimation));
		//mainArea.addObject(new GateObject(888, 0, GateObject.GATEOBJECT_HEIGHT, GateObject.GATEOBJECT_WIDTH, "test gate", assets.doorAnimationRotated));
		areas[MAIN_AREA_NUM] = mainArea;

		pathToEntranceArea = new Area(PATHTOENTRANCE_AREA_NUM, 4, 16, 0, assets.pathToEntranceAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				//entry line to main area
				// if (y <= getY() - 5) {
				if (y <= getY()) {
					return mainArea;
				}
				// else if (y >= 3136+16)
				else if (y >= 3136)
					return pathToExitArea;
				else
					return this;
			}
		};
		pathToEntranceArea.setBounds(784, 1088+1536, 1024, 512);
		pathToEntranceArea.setName("passage to entrance");
		//add objects to the area
		pathToEntranceArea.addObject(entranceWall);
		pathToEntranceArea.addObject(mainGate1);
		pathToEntranceArea.addObject(mainGate2);
		areas[PATHTOENTRANCE_AREA_NUM] = pathToEntranceArea;

		westPassageArea = new Area(WESTPASSAGE_AREA_NUM, 1, 0, 16, assets.westPassageAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to main area
				if (x >= getX() + 256) {
					return mainArea;
				}
				else if (x <= getX()) {
					return westHallwayArea;
				} else
					return this;
			}
		};
		westPassageArea.setBounds(784-256, 1088+512, 256, 256);
		westPassageArea.setName("west passage");
		areas[WESTPASSAGE_AREA_NUM] = westPassageArea;

		westHallwayArea = new Area(WESTHALLWAY_AREA_NUM, 13, 16, 0, assets.westHallwayAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to west passage
				if (x >= getX() + 256 && y >= 1088+512 && y <= 1088+512+256)
					return westPassageArea;
				else if (x >= 528 && y >= 576 && y <= 576+256)
					return southHallwayArea;
				else if (y >= 2176)
					return directorOfficeArea;
				else if (y <= 512)
					return serverRoomArea;
				else if (x <= 256 + 16 && y >= 1088+512 && y <= 1088+512+128)
					return office1Area;
				else if (x <= 256 + 16 && y >= 1088 + 128 && y <= 1088 + 128 + 128)
					return office2Area;
				else if (x <= 256 + 16 && y >= 1088 - 256 && y <= 1088 - 256 + 128)
					return office3Area;
				else {
					//System.out.println("check!");
					return this;
				}
			}
		};
		westHallwayArea.setBounds(256+16, 512, 256, 1664);
		westHallwayArea.setName("west hallway");
		westHallwayArea.addObject(directorOfficeDoor);
		westHallwayArea.addObject(serverRoomDoor);
		westHallwayArea.addObject(directorOfficeWall1);
		westHallwayArea.addObject(directorOfficeWall2);
		westHallwayArea.addObject(serverRoomWall1);
		westHallwayArea.addObject(serverRoomWall2);
		westHallwayArea.addObject(office1Wall);
		westHallwayArea.addObject(office1Door);
		westHallwayArea.addObject(office2Wall);
		westHallwayArea.addObject(office2Door);
		westHallwayArea.addObject(office3Wall);
		westHallwayArea.addObject(office3Door);
		areas[WESTHALLWAY_AREA_NUM] = westHallwayArea;

		southPassageArea = new Area(SOUTHPASSAGE_AREA_NUM, 1, 16, 0, assets.southPassageAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to main area
				if (y >= getY() + 256) {
					return mainArea;
				}
				else if (y <= getY()) {
					return southHallwayArea;
				} else
					return this;
			}
		};
		southPassageArea.setBounds(1168, 832, 256, 256);
		southPassageArea.setName("south passage");
		areas[SOUTHPASSAGE_AREA_NUM] = southPassageArea;

		southHallwayArea = new Area(SOUTHHALLWAY_AREA_NUM, 6, 0, 16, assets.southHallwayAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to south passage
				if (y >= 576+256) {
					return southPassageArea;
				}
				else if (x <= 528)
					return westHallwayArea;
				else if (y <= 576 && x >= 1168 && x <= 1168+512)
					return testRoomArea;
				else if (x >= 2064)
					return developmentRoomArea;
				else
					return this;
			}
		};
		southHallwayArea.setBounds(528, 576, 1536, 256);
		southHallwayArea.setName("south hallway");
		southHallwayArea.addObject(testRoomWall);
		southHallwayArea.addObject(testRoomDoor);
		southHallwayArea.addObject(developmentRoomEntrance);
		southHallwayArea.addObject(developmentRoomWall1);
		southHallwayArea.addObject(developmentRoomWall2);
		areas[SOUTHHALLWAY_AREA_NUM] = southHallwayArea;

		testRoomArea = new Area(TESTROOM_AREA_NUM, 7, 16, 16, assets.testRoomAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (y >= 576)
					return southHallwayArea;
				else
					return this;
			}
		};
		testRoomArea.setBounds(1168, 0, 512, 576);
		testRoomArea.setName("test room");
		testRoomArea.addObject(testRoomWall);
		testRoomArea.addObject(testRoomDoor);
		testRoomArea.addObject(new WallObject(1168, 256, 192, 64));
		testRoomArea.addObject(new WallObject(1168+512-192, 256, 192, 64));
		testRoomArea.addObject(new BoxObject(1168+512-32, 576-64-32, 32, 32, "test room box1", assets.boxTexture)); //box1
		testRoomArea.addObject(new BoxObject(1168, 256-32, 32, 32, "test room box2", assets.boxTexture)); //box2
		areas[TESTROOM_AREA_NUM] = testRoomArea;

		developmentRoomArea = new Area(DEVELOPMENTROOM_AREA_NUM, 13, 16, 0, assets.developmentRoomAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (x <= 2064 && y >= 576 && y <= 576+256)
					return southHallwayArea;
				else
					return this;
			}
		};
		developmentRoomArea.setBounds(2064, 320, 512, 768);
		developmentRoomArea.setName("development room");
		developmentRoomArea.addObject(new WallObject(2064+64, 320+512+64+48, 256, 96)); //north table
		developmentRoomArea.addObject(new WallObject(2064, 320+512, 384, 64)); //north wall
		developmentRoomArea.addObject(developmentRoomEntrance);
		developmentRoomArea.addObject(developmentRoomWall1);
		developmentRoomArea.addObject(developmentRoomWall2);
		developmentRoomArea.addObject(new WallObject(2064+128, 320+352, 64, 96)); //test robot+computers
		developmentRoomArea.addObject(new WallObject(2064+128+64+48, 320+352-32, 64, 128)); //super computer
		developmentRoomArea.addObject(new WallObject(2064, 512, 384, 64)); //south wall
		developmentRoomArea.addObject(new WallObject(2064+64, 320+48, 256, 96)); //south table
		developmentRoomArea.addObject(new BoxObject(2064+512-32, 320, 32, 32, "development room box1", assets.boxTexture)); //box1
		developmentRoomArea.addObject(new BoxObject(2064+128+64+48+64, 320+352+16, 32, 32, "development room box2", assets.boxTexture)); //box2
		developmentRoomArea.addObject(new BoxObject(2064+512-32, 320+768-32, 32, 32, "development room box3", assets.boxTexture)); //box3
		areas[DEVELOPMENTROOM_AREA_NUM] = developmentRoomArea;

		directorOfficeArea = new Area(DIRECTOROFFICE_AREA_NUM, 8, 16, 0, assets.directorOfficeAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (y <= 2176)
					return westHallwayArea;
				else
					return this;
			}
		};
		directorOfficeArea.setBounds(272, 2176, 256, 256);
		directorOfficeArea.setName("director's office");
		directorOfficeArea.addObject(directorOfficeDoor);
		directorOfficeArea.addObject(new BoxObject(272, 2176+224, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "director's office box1", assets.boxTexture));
		directorOfficeArea.addObject(directorOfficeWall1);
		directorOfficeArea.addObject(directorOfficeWall2);
		directorOfficeArea.addObject(new WallObject(272+256-32, 2176+256-32, 32, 32)); //chair
		directorOfficeArea.addObject(new WallObject(272+256-96, 2176+256-32-48, 96, 48)); //desk
		directorOfficeArea.addObject(new WallObject(272+64, 2176+64, 128, 64)); //table
		areas[DIRECTOROFFICE_AREA_NUM] = directorOfficeArea;

		serverRoomArea = new Area(SERVERROOM_AREA_NUM, 7, 16, 16, assets.serverRoomAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (y >= 512)
					return westHallwayArea;
				else
					return this;
			}
		};
		serverRoomArea.setBounds(272, 256, 256, 256);
		serverRoomArea.setName("server room");
		serverRoomArea.addObject(new BoxObject(272, 256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "server room box1", assets.boxTexture));
		serverRoomArea.addObject(serverRoomDoor);
		serverRoomArea.addObject(new BoxObject(272+256-32, 256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "server room box2", assets.boxTexture));
		serverRoomArea.addObject(new WallObject(272+64, 256+96, 128, 96)); //server rack
		serverRoomArea.addObject(serverRoomWall1);
		serverRoomArea.addObject(serverRoomWall2);
		areas[SERVERROOM_AREA_NUM] = serverRoomArea;

		office1Area = new Area(OFFICE1_AREA_NUM, 9, 16, 16, assets.officeAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office1Area.setBounds(0, 1088+512, 256+16, 256);
		office1Area.setName("office 1");
		office1Area.addObject(new WallObject(0, office1Area.getY()+224, 32, 32));		//chair
		office1Area.addObject(new WallObject(32, office1Area.getY() + 160, 48, 96));	//desk
		office1Area.addObject(new WallObject(113, office1Area.getY() + 224, 32, 32));	//complexer
		office1Area.addObject(new WallObject(224, office1Area.getY() + 224, 32, 32));	//planter
		office1Area.addObject(new WallObject(0, office1Area.getY(), 113, 64));			//shelf
		office1Area.addObject(new WallObject(146, office1Area.getY(), 32, 64));			//cabinet
		office1Area.addObject(office1Wall);
		office1Area.addObject(office1Door);
		areas[OFFICE1_AREA_NUM] = office1Area;

		office2Area = new Area(OFFICE2_AREA_NUM, 11, 16, 16, assets.officeAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office2Area.setBounds(0, 1088+128, 256+16, 256);
		office2Area.setName("office 2");
		office2Area.addObject(new WallObject(0, office2Area.getY() + 224, 32, 32)); // chair
		office2Area.addObject(new WallObject(32, office2Area.getY() + 160, 48, 96)); // desk
		office2Area.addObject(new WallObject(113, office2Area.getY() + 224, 32, 32)); // complexer
		office2Area.addObject(new WallObject(224, office2Area.getY() + 224, 32, 32)); // planter
		office2Area.addObject(new WallObject(0, office2Area.getY(), 113, 64)); // shelf
		office2Area.addObject(new WallObject(146, office2Area.getY(), 32, 64)); // cabinet
		office2Area.addObject(new BoxObject(0, office2Area.getY() + 64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 2 box1", assets.boxTexture));
		office2Area.addObject(new BoxObject(224, office2Area.getY() + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 2 box2", assets.boxTexture));
		office2Area.addObject(office2Wall);
		office2Area.addObject(office2Door);
		areas[OFFICE2_AREA_NUM] = office2Area;

		office3Area = new Area(OFFICE3_AREA_NUM, 13, 16, 16, assets.officeAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office3Area.setBounds(0, 1088-256, 256+16, 256);
		office3Area.setName("office 3");
		office3Area.addObject(new WallObject(0, office3Area.getY() + 224, 32, 32)); // chair
		office3Area.addObject(new WallObject(32, office3Area.getY() + 160, 48, 96)); // desk
		office3Area.addObject(new WallObject(113, office3Area.getY() + 224, 32, 32)); // complexer
		office3Area.addObject(new WallObject(224, office3Area.getY() + 224, 32, 32)); // planter
		office3Area.addObject(new WallObject(0, office3Area.getY(), 113, 64)); // shelf
		office3Area.addObject(new WallObject(146, office3Area.getY(), 32, 64)); // cabinet
		office3Area.addObject(new BoxObject(0, office3Area.getY() + 64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box1", assets.boxTexture));
		office3Area.addObject(new BoxObject(224, office3Area.getY() + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box2", assets.boxTexture));
		office3Area.addObject(new BoxObject(0, office3Area.getY() + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box3", assets.boxTexture));
		office3Area.addObject(new BoxObject(80, office3Area.getY() + 256-128, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box4", assets.boxTexture));
		office3Area.addObject(office3Wall);
		office3Area.addObject(office3Door);
		areas[OFFICE3_AREA_NUM] = office3Area;

		pathToExitArea = new Area(PATHTOEXIT_AREA_NUM, 1, 16, 0, assets.pathToExitAreaTexture) {
			@Override
			public Area determineArea(float x, float y) {
				if (y <= 3136)
					return pathToEntranceArea;
				else
					return this;
			}
		};
		pathToExitArea.setBounds(1168, 3136, 256, 768);
		pathToExitArea.setName("passage to exit");
		areas[PATHTOEXIT_AREA_NUM] = pathToExitArea;
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
		robot = new Player("robot", assets.robotAnimations);
		robot.setSounds(assets.getRobotSounds());
		// robot.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		//robot.setPos(0, 0); // sets the robot's initial position to the middle
		robot.setCurrentArea(mainArea);
		robot.setPosition(784 + 10, 1088 + 10);

		player1 = new Player("player1", assets.playerAnimations);
		player1.setSounds(assets.getHumanSounds());
		//player1.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		//player1.setPos(50, 200); //sets the player1's position on the upper left quarter of the map
		player1.setCurrentArea(mainArea);
		player1.setPosition(784 + 95, 1088 + 95);

		player2 = new Player("player2", assets.playerAnimations);
		//player2.setSounds(assets.humanSounds);
		// //player2.setPosRange(0, Prototype.MAP_WIDTH - Prototype.CHAR_WIDTH, 0, Prototype.MAP_HEIGHT - Prototype.CHAR_HEIGHT);
		// player2.setPos(Prototype.MAP_WIDTH/2 - Prototype.CHAR_WIDTH/2, Prototype.MAP_HEIGHT/2 - Prototype.CHAR_HEIGHT/2); //sets the player2's position on the bottom left quarter of the map
		player2.setCurrentArea(mainArea);
		player2.setPosition(player1.getX() + 95, player1.getY());

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
		players[2] = player2;

		//create card key object
		cardKey = new CardKeyObject(this, null, -1, -1, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key", assets.cardKeyTexture);
		//cardKey = new CardKeyObject(this, mainArea, mainArea.getX()+512, mainArea.getY()+896, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key", assets.cardKeyTexture);
		//mainArea.addObject(cardKey);

		//configure action
		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			@Override
			public boolean act(float delta) {
				for (Area area : areas) {
					area.act(delta);
				}
				for (Player player : players) {
					player.act(delta);
				}

				return true;
			}

		});

		addAction(loop);
	}

	public void setRemainTime(long remain) {
		remainTime = remain;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//System.out.println("init color: " + batch.getColor().r + ", " + batch.getColor().g + ", " + batch.getColor().b + ", " + batch.getColor().a);
		for (Area area : areas) {
			//Gdx.app.log("World", "area: (" + area.getWidth() + ", " + area.getHeight() + ")");
			assets.baseTexture.setRegion(0, 0, (int)area.getWidth(), (int)area.getHeight());
			batch.draw(assets.baseTexture, area.getX(), area.getY());
			area.draw(batch, parentAlpha);
		}
		//to draw robot at the last(projectile should be drawn on the very top)
		for (int i = players.length - 1; i != -1; i--) {
		//for (Player player : players) {
			players[i].draw(batch, parentAlpha);
		}
		if (cardKey.getX() != -1f) {
			cardKey.draw(batch, parentAlpha);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		assets.dispose(); //unnecessary
		robot.dispose(); //unnecessary
		player1.dispose(); //unnecessary
		// player2.dispose();
		// player3.dispose();
		// player4.dispose();
		//area1.dispose();
		//area2.dispose();
		//area3.dispose();

		Gdx.app.log("World", "disposed");
	}
}
