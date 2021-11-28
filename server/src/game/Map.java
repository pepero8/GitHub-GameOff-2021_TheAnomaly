package game;

public class Map {
	/*adding new area
	  1. add AREA_NAME_NUM
	  2. add area global variable
	  3. increase size of areas by 1
	  4. instantiate the area & add objects
	  5. insert area into areas
	  6. update determineArea() of all the other areas
	  7. add movable space if necessary. Update all the other spaces' determineSpace()
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

	public static final int EXIT_AREA_NUM = 14;

	// MovableSpace spaceMainNorth;
	// MovableSpace spaceMainEast;
	// MovableSpace spaceMainWest;
	// MovableSpace spaceMainSouth;
	MovableSpace spaceMiddleArea;
	MovableSpace spaceWestPassage;
	MovableSpace spaceWestArea;
	MovableSpace spaceSouthPassage;
	MovableSpace spaceSouthHallway;
	MovableSpace spaceTestRoom;
	MovableSpace spaceDevelopmentRoom;
	MovableSpace spaceOffice1;
	MovableSpace spaceOffice2;
	MovableSpace spaceOffice3;
	MovableSpace spacePathToExit;

	Area mainArea;
	Area pathToEntranceArea;
	Area westPassageArea;
	Area westHallwayArea;
	Area southPassageArea;
	Area southHallwayArea;
	Area testRoomArea;
	Area developmentRoomArea;
	Area directorOfficeArea;
	Area serverRoomArea;
	Area office1Area;
	Area office2Area;
	Area office3Area;
	Area pathToExitArea;

	Area exitArea;

	Area[] areas;

	CardKeyObject cardKey;

	Map() {
		areas = new Area[NUM_AREAS];

		spaceMiddleArea = new MovableSpace(784, 1088, 1024-World.PLAYER_WIDTH, 2048-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				// if (playerX >= 224 && playerY <= 1304 && playerY >= 1304 - 224) {
				// 	return spaceMainNorth;
				// } else if (playerX >= 224 && playerY >= 0 && playerY <= 224) {
				// 	// System.out.println("MainWest - playerX: " + playerX);
				// 	return spaceMainSouth;
				// } else
				if (playerX <= 784 && playerY >= 1088+512 && playerY <= 1088+768-World.PLAYER_HEIGHT)
					return spaceWestPassage;
				else if (playerY <= 1088 && playerX >= 1168 && playerX <= 1168+256-World.PLAYER_WIDTH)
					return spaceSouthPassage;
				else if (playerY > 3136-World.PLAYER_HEIGHT && playerX >= 1168 && playerX <= 1168+256-World.PLAYER_WIDTH)
					return spacePathToExit;
				else
					return this;
			}
		};

		spaceWestPassage = new MovableSpace(784-256-World.PLAYER_WIDTH, 1088+512, 256+World.PLAYER_WIDTH, 256-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX > 784/* && playerY >= 1088 + 512 && playerY <= 1088 + 768*/)
					return spaceMiddleArea;
				else if (playerX <= 512+16-World.PLAYER_WIDTH)
					return spaceWestArea;
				else
					return this;
			}
		};

		spaceWestArea = new MovableSpace(256+16, 256, 256-World.PLAYER_WIDTH, 1664+512-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX > 512 + 16 - World.PLAYER_WIDTH && playerY >= 1088+512 && playerY <= 1088+768-World.PLAYER_HEIGHT)
					return spaceWestPassage;
				else if (playerX > 528-World.PLAYER_WIDTH && playerY >= 576 && playerY <= 576+256-World.PLAYER_HEIGHT)
					return spaceSouthHallway;
				else if (playerX < 256+16 && playerY >= 1088+512 && playerY <= 1088+512+256-World.PLAYER_HEIGHT)
					return spaceOffice1;
				else if (playerX < 256+16 && playerY >= 1088+128 && playerY <= 1088+128+256-World.PLAYER_HEIGHT)
					return spaceOffice2;
				else if (playerX < 256+16 && playerY >= 1088-256 && playerY <= 1088-256+256-World.PLAYER_HEIGHT)
					return spaceOffice3;
				else
					return this;
			}
		};

		spaceSouthPassage = new MovableSpace(1168, 832-World.PLAYER_HEIGHT, 256-World.PLAYER_WIDTH, 256+World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerY > 1088)
					return spaceMiddleArea;
				else if (playerY < 832-World.PLAYER_HEIGHT)
					return spaceSouthHallway;
				else
					return this;
			}
		};

		spaceSouthHallway = new MovableSpace(528-World.PLAYER_WIDTH, 576, 1536+World.PLAYER_WIDTH, 256-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX <= 528-World.PLAYER_WIDTH)
					return spaceWestArea;
				else if (playerY >= 832-World.PLAYER_HEIGHT && playerX >= 1168 && playerX <= 1168+256-World.PLAYER_WIDTH)
					return spaceSouthPassage;
				else if (playerY < 576 && playerX >= 1168 && playerX <= 1168+128)
					return spaceTestRoom;
				else if (playerX >= 2064)
					return spaceDevelopmentRoom;
				else
					return this;
			}
		};

		spaceTestRoom = new MovableSpace(1168, 0, 512-World.PLAYER_WIDTH, 576) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerY >= 576)
					return spaceSouthHallway;
				else
					return this;
			}
		};

		spaceDevelopmentRoom = new MovableSpace(2064, 320, 512-World.PLAYER_WIDTH, 768-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX < 2064 && playerY >= 320+256 && playerY <= 320+512-World.PLAYER_HEIGHT)
					return spaceSouthHallway;
				else
					return this;
			}
		};

		spaceOffice1 = new MovableSpace(0, 1088+512, 256+16, 256-World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX >= 256+16)
					return spaceWestArea;
				else
					return this;
			}
		};

		spaceOffice2 = new MovableSpace(0, 1088 + 128, 256 + 16, 256 - World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX >= 256 + 16)
					return spaceWestArea;
				else
					return this;
			}
		};

		spaceOffice3 = new MovableSpace(0, 1088 - 256, 256 + 16, 256 - World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX >= 256 + 16)
					return spaceWestArea;
				else
					return this;
			}
		};

		spacePathToExit = new MovableSpace(1168, 3136-World.PLAYER_HEIGHT, 256-World.PLAYER_WIDTH, 768+World.PLAYER_HEIGHT) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerY <= 3136-World.PLAYER_HEIGHT && playerX >= 1168 && playerX <= 1168+256)
					return spaceMiddleArea;
				else
					return this;
			}
		};

		// spaceMainWest = new MovableSpace(0, 0, 224, 1304) {
		// 	@Override
		// 	MovableSpace determineSpace(float playerX, float playerY) {
		// 		// TODO Auto-generated method stub
		// 		if (playerX >= 224 && playerY <= 1304 && playerY >= 1304-224) {
		// 			return spaceMainNorth;
		// 		}
		// 		else if (playerX >= 224 && playerY >= 0 && playerY <= 224) {
		// 			//System.out.println("MainWest - playerX: " + playerX);
		// 			return spaceMainSouth;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };

		// spaceMainNorth = new MovableSpace(224, 1304-224, 664, 224) {
		// 	@Override
		// 	MovableSpace determineSpace(float x, float y) {
		// 		// TODO Auto-generated method stub
		// 		if (x <= 224 && y >= 1304-224 && y <= 1304) {
		// 			return spaceMainWest;
		// 		}
		// 		else if (x >= 224+664 && y >= 1304-224 && y <= 1304) {
		// 			return spaceMainEast;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };

		// spaceMainEast = new MovableSpace(224+664, 0, 224, 1304) {
		// 	@Override
		// 	MovableSpace determineSpace(float x, float y) {
		// 		// TODO Auto-generated method stub
		// 		if (x <= 224+664 && y >= 1304-224 && y <= 1304) {
		// 			return spaceMainNorth;
		// 		}
		// 		else if (x <= 224 + 664 && y >= 0 && y <= 224) {
		// 			return spaceMainSouth;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };

		// spaceMainSouth = new MovableSpace(224, 0, 664, 224) {
		// 	@Override
		// 	MovableSpace determineSpace(float playerX, float playerY) {
		// 		// TODO Auto-generated method stub
		// 		if (playerX >= 224 + 664 && playerY >= 0 && playerY <= 224) {
		// 			return spaceMainEast;
		// 		}
		// 		else if (playerX <= 224 && playerY >= 0 && playerY <= 224) {
		// 			//System.out.println("MainSouth - x: " + playerX);
		// 			return spaceMainWest;
		// 		}
		// 		else
		// 			return this;
		// 	}
		// };

		//doors & gates & walls
		GateObject mainGate1 = new GateObject(784+0, 1088+1536-5, GateObject.GATEOBJECT_WIDTH, GateObject.GATEOBJECT_HEIGHT+5, "entrance gate1");
		GateObject mainGate2 = new GateObject(784+256*3, 1088+1536-5, GateObject.GATEOBJECT_WIDTH, GateObject.GATEOBJECT_HEIGHT+5, "entrance gate2");
		WallObject entranceWall = new WallObject(784 + 256, 1088 + 1536 - 5, 512, 256 + 5);

		DoorObject developmentRoomEntrance = new DoorObject(2048, 640, 16, 128, "development room entrance");
		WallObject developmentRoomWall1 = new WallObject(2048 + 1, 768, 16, 64);
		WallObject developmentRoomWall2 = new WallObject(2048 + 1, 576, 16, 64);

		DoorObject directorOfficeDoor = new DoorObject(256 + 16 + 64, 512 + 1600, 128, 64, "director's office door");
		WallObject directorOfficeWall1 = new WallObject(256 + 16, 512 + 1600, 64, 64);
		WallObject directorOfficeWall2 = new WallObject(256 + 16 + 256 - 64, 512 + 1600, 64, 64);
		
		DoorObject testRoomDoor = new DoorObject(1168, 512, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test room entrance");
		WallObject testRoomWall = new WallObject(1168 + 128, 512, 512 - 128, 64+1);
		
		DoorObject serverRoomDoor = new DoorObject(256 + 16 + 64, 512, 128, 64, "server room door");
		WallObject serverRoomWall1 = new WallObject(256 + 16, 512, 64, 64);
		WallObject serverRoomWall2 = new WallObject(256 + 16 + 256 - 64, 512, 64, 64);
		
		DoorObject office1Door = new DoorObject(256, 1088 + 512, 16, 128, "office 1 door");
		WallObject office1Wall = new WallObject(256, 1088 + 512 + 128, 16, 128);

		DoorObject office2Door = new DoorObject(256, 1088 + 128, 16, 128, "office 2 door");
		WallObject office2Wall = new WallObject(256, 1088 + 128 + 128, 16, 128);
		
		DoorObject office3Door = new DoorObject(256, 1088 - 256, 16, 128, "office 3 door");
		WallObject office3Wall = new WallObject(256, 1088 - 256 + 128, 16, 128);

		//areas
		mainArea = new Area(this, 784, 1088, 1024, 1536, MAIN_AREA_NUM, 18) {
			@Override
			Area determineArea(float x, float y) {
			// Area determineArea(Player player) {
				// TODO Auto-generated method stub
				//float originY = getY();

				//entry line to pathToEntranceArea
				if (y >= this.y + 1536) {
					return pathToEntranceArea;
				}
				//else if (x <= this.x - 16) {
				else if (x <= this.x && y >= 1088 + 512 && y <= 1088 + 512 + 256) { //to change projectile's area before hitWall() is processed
					return westPassageArea;
				}
				// else if (y <= this.y - 16) {
				else if (y <= this.y && x >= 1168 && x <= 1168 + 256) {
					return southPassageArea;
				}
				else
					return this;
			}
		};
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
		mainArea.addObject(new WallObject(1056 + 192, 1792 + 176, 96, 160)); // table
		mainArea.addObject(new WallObject(1056 + 192 + 32, 1792 + 176 + 160, 32, 32)); // chair1
		//mainArea.addObject(new WallObject(1056 + 192 + 96, 1792 + 176 + 160 - 32, 32, 32)); // chair2
		//mainArea.addObject(new WallObject(1056 + 192 + 96, 1792 + 176 + 160 - 96, 32, 32)); // chair3
		mainArea.addObject(new WallObject(1056 + 192 + 96, 1792 + 176, 32, 160)); // chair4
		mainArea.addObject(new WallObject(1056 + 192 - 32, 1792 + 176, 32, 160)); // chair5
		//mainArea.addObject(new WallObject(1056 + 192 - 32, 1792 + 176 + 64, 32, 32)); // chair6
		//mainArea.addObject(new WallObject(1056 + 192 - 32, 1792 + 176 + 160 - 32, 32, 32)); // chair7
		mainArea.addObject(new BoxObject(1056, 1792, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box1"));
		mainArea.addObject(new BoxObject(1056+480-32, 1792+512-32, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box2"));
		mainArea.addObject(new BoxObject(1056+480-32, 1792+160, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "conference room box3"));
		//mainArea.addObject(new DoorObject(0, 668, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test door"));
		//mainArea.addObject(new GateObject(888, 0, GateObject.GATEOBJECT_HEIGHT, GateObject.GATEOBJECT_WIDTH, "test gate"));
		//mainArea.addObject(new CardKeyObject(mainArea, mainArea.x+512, mainArea.y+896, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key"));
		areas[MAIN_AREA_NUM] = mainArea;

		pathToEntranceArea = new Area(this, 784, 1088+1536, 1024, 512, PATHTOENTRANCE_AREA_NUM, 3+1) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				// entry line to main area
				// if (y <= this.y - 5) {
				if (y <= this.y) {
					return mainArea;
				}
				// else if (y >= 3136 + 16)
				else if (y >= 3136)
					return pathToExitArea;
				else
					return this;
			}
		};
		// add objects to the area
		pathToEntranceArea.addObject(entranceWall);
		pathToEntranceArea.addObject(mainGate1);
		pathToEntranceArea.addObject(mainGate2);
		areas[PATHTOENTRANCE_AREA_NUM] = pathToEntranceArea;

		westPassageArea = new Area(this, 784-256, 1088+512, 256, 256, WESTPASSAGE_AREA_NUM, 1) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				// entry line to main area
				if (x >= this.x + 256) {
					return mainArea;
				} else if (x <= this.x) {
					return westHallwayArea;
				} else
					return this;
			}
		};
		areas[WESTPASSAGE_AREA_NUM] = westPassageArea;

		westHallwayArea = new Area(this, 256+16, 512, 256, 1664, WESTHALLWAY_AREA_NUM, 13) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				// entry line to west passage
				if (x >= this.x + 256 && y >= 1088+512 && y <= 1088+512+256) {
					return westPassageArea;
				}
				else if (x >= 256+16+256 && y >= 576 && y <= 576 + 256)
					return southHallwayArea;
				else if (y >= 512+1664)
					return directorOfficeArea;
				else if (y <= 512)
					return serverRoomArea;
				else if (x <= 256 + 16 && y >= 1088+512 && y <= 1088+512 + 128)
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
		//add objects to the area
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
	
		southPassageArea = new Area(this, 1168, 832, 256, 256, SOUTHPASSAGE_AREA_NUM, 1) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				// entry line to main area
				if (y >= this.y + 256) {
					return mainArea;
				}
				else if (y <= this.y)
					return southHallwayArea;
				else
					return this;
			}
		};
		areas[SOUTHPASSAGE_AREA_NUM] = southPassageArea;

		southHallwayArea = new Area(this, 528, 576, 1536, 256, SOUTHHALLWAY_AREA_NUM, 6) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				// entry line to south passage
				if (y >= 576 + 256) {
					return southPassageArea;
				} else if (x <= 528)
					return westHallwayArea;
				else if (y <= 576 && x >= 1168 && x <= 1168 + 512)
					return testRoomArea;
				else if (x >= 2064)
					return developmentRoomArea;
				else
					return this;
			}
		};
		southHallwayArea.addObject(testRoomWall);
		southHallwayArea.addObject(testRoomDoor);
		southHallwayArea.addObject(developmentRoomEntrance);
		southHallwayArea.addObject(developmentRoomWall1);
		southHallwayArea.addObject(developmentRoomWall2);
		areas[SOUTHHALLWAY_AREA_NUM] = southHallwayArea;
	
		testRoomArea = new Area(this, 1168, 0, 512, 576, TESTROOM_AREA_NUM, 7) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (y >= 576)
					return southHallwayArea;
				else
					return this;
			}
		};
		testRoomArea.addObject(testRoomWall);
		testRoomArea.addObject(testRoomDoor);
		testRoomArea.addObject(new WallObject(1168, 256, 192, 64));
		testRoomArea.addObject(new WallObject(1168+512-192, 256, 192, 64));
		testRoomArea.addObject(new BoxObject(1168+512-32, 576-64-32, 32, 32, "test room box1")); //box1
		testRoomArea.addObject(new BoxObject(1168, 256-32, 32, 32, "test room box2")); //box2
		areas[TESTROOM_AREA_NUM] = testRoomArea;

		developmentRoomArea = new Area(this, 2064, 320, 512, 768, DEVELOPMENTROOM_AREA_NUM, 13) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (x <= 2064 && y >= 576 && y <= 576 + 256)
					return southHallwayArea;
				else
					return this;
			}
		};
		developmentRoomArea.addObject(new WallObject(2064+64, 320+512+64+48, 256, 96)); //north table
		developmentRoomArea.addObject(new WallObject(2064, 320+512, 384, 64)); //north wall
		developmentRoomArea.addObject(developmentRoomEntrance);
		developmentRoomArea.addObject(developmentRoomWall1);
		developmentRoomArea.addObject(developmentRoomWall2);
		developmentRoomArea.addObject(new WallObject(2064+128, 320+352, 64, 96)); //test robot+computers
		developmentRoomArea.addObject(new WallObject(2064+128+64+48, 320+352-32, 64, 128)); //super computer
		developmentRoomArea.addObject(new WallObject(2064, 512, 384, 64)); //south wall
		developmentRoomArea.addObject(new WallObject(2064+64, 320+48, 256, 96)); //south table
		developmentRoomArea.addObject(new BoxObject(2064+512-32, 320, 32, 32, "development room box1")); //box1
		developmentRoomArea.addObject(new BoxObject(2064+128+64+48+64, 320+352+16, 32, 32, "development room box2")); //box2
		developmentRoomArea.addObject(new BoxObject(2064+512-32, 320+768-32, 32, 32, "development room box3")); //box3
		areas[DEVELOPMENTROOM_AREA_NUM] = developmentRoomArea;
	
		directorOfficeArea = new Area(this, 272, 2176, 256, 256, DIRECTOROFFICE_AREA_NUM, 8) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (y <= 2176)
					return westHallwayArea;
				else
					return this;
			}
		};
		directorOfficeArea.addObject(directorOfficeDoor);
		directorOfficeArea.addObject(new BoxObject(272, 2176+224, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "director's office box1"));
		directorOfficeArea.addObject(directorOfficeWall1);
		directorOfficeArea.addObject(directorOfficeWall2);
		directorOfficeArea.addObject(new WallObject(272 + 256 - 32, 2176 + 256 - 32, 32, 32)); // chair
		directorOfficeArea.addObject(new WallObject(272 + 256 - 96, 2176 + 256 - 32 - 48, 96, 48)); // desk
		directorOfficeArea.addObject(new WallObject(272 + 64, 2176 + 64, 128, 64)); // table
		areas[DIRECTOROFFICE_AREA_NUM] = directorOfficeArea;
	
		serverRoomArea = new Area(this, 272, 256, 256, 256, SERVERROOM_AREA_NUM, 7) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (y >= 512)
					return westHallwayArea;
				else
					return this;
			}
		};
		serverRoomArea.addObject(new BoxObject(272, 256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "server room box1"));
		serverRoomArea.addObject(serverRoomDoor);
		serverRoomArea.addObject(new BoxObject(272+256-32, 256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "server room box2"));
		serverRoomArea.addObject(new WallObject(272 + 64, 256 + 96, 128, 96)); // server rack
		serverRoomArea.addObject(serverRoomWall1);
		serverRoomArea.addObject(serverRoomWall2);
		areas[SERVERROOM_AREA_NUM] = serverRoomArea;
	
		office1Area = new Area(this, 0, 1088+512, 256+16, 256, OFFICE1_AREA_NUM, 9) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office1Area.addObject(new WallObject(0, office1Area.y + 224, 32, 32)); // chair
		office1Area.addObject(new WallObject(32, office1Area.y + 160, 48, 96)); // desk
		office1Area.addObject(new WallObject(113, office1Area.y + 224, 32, 32)); // complexer
		office1Area.addObject(new WallObject(224, office1Area.y + 224, 32, 32)); // planter
		office1Area.addObject(new WallObject(0, office1Area.y, 113, 64)); // shelf
		office1Area.addObject(new WallObject(146, office1Area.y, 32, 64)); // cabinet
		office1Area.addObject(office1Wall);
		office1Area.addObject(office1Door);
		areas[OFFICE1_AREA_NUM] = office1Area;
	
		office2Area = new Area(this, 0, 1088+128, 256+16, 256, OFFICE2_AREA_NUM, 11) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office2Area.addObject(new WallObject(0, office2Area.y + 224, 32, 32)); // chair
		office2Area.addObject(new WallObject(32, office2Area.y + 160, 48, 96)); // desk
		office2Area.addObject(new WallObject(113, office2Area.y + 224, 32, 32)); // complexer
		office2Area.addObject(new WallObject(224, office2Area.y + 224, 32, 32)); // planter
		office2Area.addObject(new WallObject(0, office2Area.y, 113, 64)); // shelf
		office2Area.addObject(new WallObject(146, office2Area.y, 32, 64)); // cabinet
		office2Area.addObject(new BoxObject(0, office2Area.y + 64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 2 box1"));
		office2Area.addObject(new BoxObject(224, office2Area.y + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 2 box2"));
		office2Area.addObject(office2Wall);
		office2Area.addObject(office2Door);
		areas[OFFICE2_AREA_NUM] = office2Area;
	
		office3Area = new Area(this, 0, 1088-256, 256+16, 256, OFFICE3_AREA_NUM, 13) {
			@Override
			public Area determineArea(float x, float y) {
			// public Area determineArea(Player player) {
				if (x >= 256 + 16)
					return westHallwayArea;
				else
					return this;
			}
		};
		office3Area.addObject(new WallObject(0, office3Area.y + 224, 32, 32)); // chair
		office3Area.addObject(new WallObject(32, office3Area.y + 160, 48, 96)); // desk
		office3Area.addObject(new WallObject(113, office3Area.y + 224, 32, 32)); // complexer
		office3Area.addObject(new WallObject(224, office3Area.y + 224, 32, 32)); // planter
		office3Area.addObject(new WallObject(0, office3Area.y, 113, 64)); // shelf
		office3Area.addObject(new WallObject(146, office3Area.y, 32, 64)); // cabinet
		office3Area.addObject(new BoxObject(0, office3Area.y + 64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box1"));
		office3Area.addObject(new BoxObject(224, office3Area.y + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box2"));
		office3Area.addObject(new BoxObject(0, office3Area.y + 256-64, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box3"));
		office3Area.addObject(new BoxObject(80, office3Area.y + 256-128, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "office 3 box4"));
		office3Area.addObject(office3Wall);
		office3Area.addObject(office3Door);
		areas[OFFICE3_AREA_NUM] = office3Area;
	
		pathToExitArea = new Area(this, 1168, 3136, 256, 768, PATHTOEXIT_AREA_NUM, 1) {
			@Override
			public Area determineArea(float x, float y) {
				if (y <= 3136)
					return pathToEntranceArea;
				else
					return this;
			}

			@Override
			public Area determineArea(Player player) {
				if (player.y <= 3136)
					return pathToEntranceArea;
				else if (player.playerNum != World.ROBOT_NUM && player.y >= 3136+768-32)
					return exitArea;
				else
					return this;
			}
		};
		areas[PATHTOEXIT_AREA_NUM] = pathToExitArea;

		System.out.println("total box number: " + BoxObject.REMAINING_BOX_OBJECTS);
	
		exitArea = new Area(this, 0, 0, 0, 0, EXIT_AREA_NUM, 0) {
			@Override
			public Area determineArea(float x, float y) {
					return this;
			}
		};
	}
}