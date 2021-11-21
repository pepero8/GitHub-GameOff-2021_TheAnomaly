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
	private static final int MAIN_AREA_NUM = 0;
	private static final int PATHTOENTRANCE_AREA_NUM = 1;
	private static final int WESTPASSAGE_AREA_NUM = 2;
	private static final int WESTHALLWAY_AREA_NUM = 3;
	private static final int SOUTHPASSAGE_AREA_NUM = 4;
	private static final int SOUTHHALLWAY_AREA_NUM = 5;
	private static final int TESTROOM_AREA_NUM = 6;
	private static final int DEVELOPMENTROOM_AREA_NUM = 7;
	private static final int DIRECTOROFFICE_AREA_NUM = 8;

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

	Area mainArea;
	Area pathToEntranceArea;
	Area westPassageArea;
	Area westHallwayArea;
	Area southPassageArea;
	Area southHallwayArea;
	Area testRoomArea;
	Area developmentRoomArea;
	Area directorOfficeArea;

	Area[] areas;

	CardKeyObject cardKey;

	Map() {
		areas = new Area[9];

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
		DoorObject testRoomDoor = new DoorObject(1168, 512, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test room entrance");
		DoorObject developmentRoomEntrance = new DoorObject(2048, 640, 16, 128, "development room entrance");
		DoorObject directorOfficeDoor = new DoorObject(256 + 16 + 64, 512 + 1600, 128, 64, "director's office door");
		WallObject directorOfficeWall1 = new WallObject(256 + 16, 512 + 1600, 64, 64);
		WallObject directorOfficeWall2 = new WallObject(256 + 16 + 256 - 64, 512 + 1600, 64, 64);
		WallObject entranceWall = new WallObject(784 + 256, 1088 + 1536 - 5, 512, 256 + 5);
		WallObject testRoomWall = new WallObject(1168 + 128, 512, 512 - 128, 64+1);
		WallObject developmentRoomWall1 = new WallObject(2048+1, 768, 16, 64);
		WallObject developmentRoomWall2 = new WallObject(2048+1, 576, 16, 64);

		//areas
		mainArea = new Area(this, 784, 1088, 1024, 1536, MAIN_AREA_NUM, 11+1) {
			@Override
			Area determineArea(float x, float y) {
				// TODO Auto-generated method stub
				//float originY = getY();

				//entry line to pathToEntranceArea
				if (y >= this.y + 1536) {
					return pathToEntranceArea;
				}
				else if (x <= this.x - 16) {
					return westPassageArea;
				}
				else if (y <= this.y - 16) {
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
		mainArea.addObject(new WallObject(784+256, 256, 1088+512, 384));
		mainArea.addObject(new BoxObject(784+572, 1088+128, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "main area box1"));
		//mainArea.addObject(new DoorObject(0, 668, DoorObject.DOOROBJECT_WIDTH, DoorObject.DOOROBJECT_HEIGHT, "test door"));
		//mainArea.addObject(new GateObject(888, 0, GateObject.GATEOBJECT_HEIGHT, GateObject.GATEOBJECT_WIDTH, "test gate"));
		//mainArea.addObject(new CardKeyObject(mainArea, 872, 240, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key"));
		areas[MAIN_AREA_NUM] = mainArea;

		pathToEntranceArea = new Area(this, 784, 1088+1536, 1024, 512, PATHTOENTRANCE_AREA_NUM, 3+1) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to main area
				if (y <= this.y - 5) {
					return mainArea;
				} else
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

		westHallwayArea = new Area(this, 256+16, 512, 256, 1664, WESTHALLWAY_AREA_NUM, 7) {
			@Override
			public Area determineArea(float x, float y) {
				// entry line to west passage
				if (x >= this.x + 256 + 16) {
					return westPassageArea;
				}
				else if (x >= 528 + 16 && y >= 576 && y <= 576 + 256)
					return southHallwayArea;
				else if (y >= 2176 - 16)
					return directorOfficeArea;
				else
					return this;
			}
		};
		//add objects to the area
		westHallwayArea.addObject(directorOfficeDoor);
		westHallwayArea.addObject(new DoorObject(256 + 16 + 64, 512, 128, 64, "server room door"));
		westHallwayArea.addObject(directorOfficeWall1);
		westHallwayArea.addObject(directorOfficeWall2);
		westHallwayArea.addObject(new WallObject(256 + 16, 512, 64, 64));
		westHallwayArea.addObject(new WallObject(256 + 16 + 256 - 64, 512, 64, 64));
		areas[WESTHALLWAY_AREA_NUM] = westHallwayArea;
	
		southPassageArea = new Area(this, 1168, 832, 256, 256, SOUTHPASSAGE_AREA_NUM, 1) {
			@Override
			public Area determineArea(float x, float y) {
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
				// entry line to south passage
				if (y >= 576 + 256 + 16) {
					return southPassageArea;
				} else if (x <= 528)
					return westHallwayArea;
				else if (y <= 560)
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
	
		testRoomArea = new Area(this, 1168, 0, 512, 576, TESTROOM_AREA_NUM, 3) {
			@Override
			public Area determineArea(float x, float y) {
				if (y >= 576)
					return southHallwayArea;
				else
					return this;
			}
		};
		testRoomArea.addObject(testRoomWall);
		testRoomArea.addObject(testRoomDoor);
		areas[TESTROOM_AREA_NUM] = testRoomArea;

		developmentRoomArea = new Area(this, 2064, 320, 512, 768, DEVELOPMENTROOM_AREA_NUM, 6) {
			@Override
			public Area determineArea(float x, float y) {
				if (x <= 2064 - 16)
					return southHallwayArea;
				else
					return this;
			}
		};
		developmentRoomArea.addObject(new BoxObject(2064+256, 320+256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "development room box1"));
		developmentRoomArea.addObject(new BoxObject(2064+256, 320+256+256, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "development room box2"));
		developmentRoomArea.addObject(developmentRoomEntrance);
		developmentRoomArea.addObject(developmentRoomWall1);
		developmentRoomArea.addObject(developmentRoomWall2);
		areas[DEVELOPMENTROOM_AREA_NUM] = developmentRoomArea;
	
		directorOfficeArea = new Area(this, 272, 2176, 256, 256, DIRECTOROFFICE_AREA_NUM, 5) {
			@Override
			public Area determineArea(float x, float y) {
				if (y <= 2176 - 48)
					return westHallwayArea;
				else
					return this;
			}
		};
		directorOfficeArea.addObject(directorOfficeDoor);
		directorOfficeArea.addObject(new BoxObject(272, 2176+224, BoxObject.BOXOBJECT_WIDTH, BoxObject.BOXOBJECT_HEIGHT, "director's office box1"));
		directorOfficeArea.addObject(directorOfficeWall1);
		directorOfficeArea.addObject(directorOfficeWall2);
		areas[DIRECTOROFFICE_AREA_NUM] = directorOfficeArea;
	}
}