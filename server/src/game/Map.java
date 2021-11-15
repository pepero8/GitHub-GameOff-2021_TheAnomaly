package game;

public class Map {
	private static final int MAIN_AREA_NUM = 0;

	MovableSpace spaceMainNorth;
	MovableSpace spaceMainEast;
	MovableSpace spaceMainWest;
	MovableSpace spaceMainSouth;

	Area mainArea;

	Area[] areas;

	Map() {
		areas = new Area[1];

		spaceMainWest = new MovableSpace(0, 0, 224, 1304) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX >= 224 && playerY <= 1304 && playerY >= 1304-224) {
					return spaceMainNorth;
				}
				else if (playerX >= 224 && playerY >= 0 && playerY <= 224) {
					//System.out.println("MainWest - playerX: " + playerX);
					return spaceMainSouth;
				}
				else
					return this;
			}
		};

		spaceMainNorth = new MovableSpace(224, 1304-224, 664, 224) {
			@Override
			MovableSpace determineSpace(float x, float y) {
				// TODO Auto-generated method stub
				if (x <= 224 && y >= 1304-224 && y <= 1304) {
					return spaceMainWest;
				}
				else if (x >= 224+664 && y >= 1304-224 && y <= 1304) {
					return spaceMainEast;
				}
				else
					return this;
			}
		};

		spaceMainEast = new MovableSpace(224+664, 0, 224, 1304) {
			@Override
			MovableSpace determineSpace(float x, float y) {
				// TODO Auto-generated method stub
				if (x <= 224+664 && y >= 1304-224 && y <= 1304) {
					return spaceMainNorth;
				}
				else if (x <= 224 + 664 && y >= 0 && y <= 224) {
					return spaceMainSouth;
				}
				else
					return this;
			}
		};

		spaceMainSouth = new MovableSpace(224, 0, 664, 224) {
			@Override
			MovableSpace determineSpace(float playerX, float playerY) {
				// TODO Auto-generated method stub
				if (playerX >= 224 + 664 && playerY >= 0 && playerY <= 224) {
					return spaceMainEast;
				}
				else if (playerX <= 224 && playerY >= 0 && playerY <= 224) {
					//System.out.println("MainSouth - x: " + playerX);
					return spaceMainWest;
				}
				else
					return this;
			}
		};

		mainArea = new Area(0, 0, 1144, 1336, MAIN_AREA_NUM, 1) {
			@Override
			Area determineArea(float x, float y) {
				// TODO Auto-generated method stub
				return this;
			}
		};
		mainArea.addObject(new BoxObject(572, 128, "main area box1"));
		areas[MAIN_AREA_NUM] = mainArea;
	}
}