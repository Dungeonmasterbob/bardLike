package me.bloodarowman.bardlike;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * A class representing an entire dungeon level of the game.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class GameMap {
	private MobDictionary mobDictionary;
    private boolean act = false;
	private int width, height, level, drawW, drawH;
	private Tile[][] tiles;

	public GameMap(int w, int h, int level, MobDictionary mobDictionary) {
        drawW = (int) (Math.ceil(Main.game.getScreenWidth()/64) + 1.0) / 2;
        drawH = (int) (Math.ceil(Main.game.getScreenHeight()/64) + 1.0) / 2;
		this.mobDictionary = mobDictionary;
		width = w;
		height = h;
		this.level = level;
	}

	public void draw(Graphics g, Player ply, Camera cam) {
		for (int x = Misc.clamp(ply.getTile().getTileX() - drawW, 0, this.getWidth()); x < Misc.clamp(ply.getTile().getTileX() + (drawW + 1), 0, this.getWidth()); x++) {
			for (int y = Misc.clamp(ply.getTile().getTileY() - drawH, 0, this.getHeight()); y < Misc.clamp(ply.getTile().getTileY() + (drawH + 2), 0, this.getHeight()); y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.draw(g, x * tile.getWidth(), y * tile.getHeight());
				}
			}
		}
        for (int x = Misc.clamp(ply.getTile().getTileX() - drawW, 0, this.getWidth()); x < Misc.clamp(ply.getTile().getTileX() + drawW, 0, this.getWidth()); x++) {
            for (int y = Misc.clamp(ply.getTile().getTileY() - drawH, 0, this.getHeight()); y < Misc.clamp(ply.getTile().getTileY() + drawH, 0, this.getHeight()); y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.drawEnts(g, x * tile.getWidth(), y * tile.getHeight());
				}
			}
		}
	}

    public void setAct(boolean bool) {
        this.act = bool;
    }

    public boolean isActing() {
        return act;
    }
	
	public void update(GameContainer container, StateBasedGame s, int delta) { // This is where all the mobs move
        if (!act) return;
        act = false;
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.update(container, s, delta);
                    tile.updateAttacks();
				}
			}
		}
        /*
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					ArrayList<Entity> mobs = tile.findType(Mob.class);
					if (mobs != null) { 
						Mob mob = (Mob) mobs.get(0);
						mob.setMoved(false);
					}
				}
			}
		}*/
	}
	
	public void updateAttacks() { // This is where all the mobs attack.
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				Tile tile = tiles[x][y];
				if (tile != null) {
					tile.updateAttacks();
				}
			}
		}
	}
		
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= tiles.length || y >= tiles[0].length) {
			return null;
		}
		return tiles[x][y];
	}

	public Tile getTile(Vector v) {
		if (v.getX() < 0 || v.getY() < 0 || v.getX() >= tiles.length || v.getY() >= tiles[0].length) {
			return null;
		}
		return tiles[v.getX()][v.getY()];
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public int getScaledWidth() {
		return width * 64;
	}

    public int getWidth() {
        return width;
    }

	public int getScaledHeight() {
		return height * 64;
	}

    public int getHeight() {
        return height;
    }
	
	public void regen() {
		tiles = DungeonGenerator.generateDungeon(Misc.DUNGEON_SIZE,
				Misc.DUNGEON_SIZE, mobDictionary).getTiles().clone();
	}
	
	public String toString() {
		return "(GameMap | level: " + level + ")";
	}
}
