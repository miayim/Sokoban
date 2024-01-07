import java.awt.Color;
import java.util.*;
import javalib.worldimages.*;
import tester.Tester;
import javalib.funworld.*;

//represents a piece of the ground content in Sokoban
interface IGroundContent {

  // Determines if a target is empty meaning it does not have a a trophy of the
  // same color on top of it
  boolean emptyTarget(ILevelContent c);

  // renders a piece of ground content as an image
  WorldImage renderPiece();

  // determines if this piece of ground content can cause movable content pieces
  // to slide
  boolean canSlide();

}

//represents a blank piece of ground content
class BlankGroundContent implements IGroundContent {

  // Returns false since a blank content cannot be an empty target
  public boolean emptyTarget(ILevelContent c) {
    return false;
  }

  // renders this BlankGroundContent as an image
  public WorldImage renderPiece() {
    return new FromFileImage("blank.png");
  }

  // returns false since a player or movable object cannot slide on blank content
  public boolean canSlide() {
    return false;
  }
}

//represents a target, a piece of ground content, that is not movable and is of a given color
class Target implements IGroundContent {
  String color;

  Target(String color) {
    this.color = color;
  }

  // Checks to see if the target is empty, meaning it does not have a a trophy of
  // the same color on top of it
  public boolean emptyTarget(ILevelContent c) {
    return (c.wrongColor(this.color));
  }

  // renders this Target as an image
  public WorldImage renderPiece() {
    if (this.color.equals("green")) {
      return new FromFileImage("green_target.png");
    }
    else if (this.color.equals("red")) {
      return new FromFileImage("red_target.png");
    }
    else if (this.color.equals("blue")) {
      return new FromFileImage("blue_target.png");
    }
    else if (this.color.equals("yellow")) {
      return new FromFileImage("yellow_target.png");
    }
    else {
      throw new RuntimeException("Cannot render!");
    }
  }

  // returns false since a player or movable object cannot slide on a target
  public boolean canSlide() {
    return false;
  }
}

// represents a piece of ice on which a player and movable objects can slide
class Ice implements IGroundContent {

  // Returns false since Ice cannot be an empty target
  public boolean emptyTarget(ILevelContent c) {
    return false;
  }

  // renders this Ice as an image
  public WorldImage renderPiece() {
    return new FromFileImage("ice.png");
  }

  // returns true since a player or movable object can slide on ice
  public boolean canSlide() {
    return true;
  }

}

// represents a piece of the level content in Sokoban
interface ILevelContent {

  // renders a piece of Level content as an image
  WorldImage renderPiece();

  // returns true if this level content is movable on key press (is a player)
  boolean movableOnKey();

  // returns true if this level content can hold a player (is a blank)
  boolean canHoldPlayer();

  // returns this level content to a blank content
  ILevelContent changeToBlank();

  // changes this level content to the given level content
  ILevelContent changeToGiven(ILevelContent given);

  // Returns true if the color does not match the given color
  boolean wrongColor(String color);

  // Returns true if this level content can be pushed by a player
  boolean canBePushed();

  // Returns true if this level content is a hole
  boolean canBeLost();
}

// to represent a piece of level content in a Sokoban level
abstract class ALevelContent implements ILevelContent {

  // renders this level content as an image
  public abstract WorldImage renderPiece();

  // only a player is movable on key press, so the abstract class should return
  // false
  // and is overridden in the player class
  public boolean movableOnKey() {
    return false;
  }

  // only a blank content can hold a player, so the abstract class should return
  // false
  // and is overridden in the blank level content class
  public boolean canHoldPlayer() {
    return false;
  }

  // returns a BlankLevelContnet
  public ILevelContent changeToBlank() {
    return new BlankLevelContent();
  }

  // changes this level content to the given level content
  public ILevelContent changeToGiven(ILevelContent given) {
    return given;
  }

  // Returns true since a every class except trophies not have a color and
  // therefore is the
  // wrong color and the method is overridden in the trophy case
  public boolean wrongColor(String color) {
    return true;
  }

  // only a trophy or box can be pushed by the player, so the abstract class
  // should return false
  // and is overridden in the trophy and box class
  public boolean canBePushed() {
    return false;
  }

  // only a hole can cause content to be lost, so the abstract class should return
  // false
  // and is overridden in the hole class
  public boolean canBeLost() {
    return false;
  }
}

// represents a blank piece of level content
class BlankLevelContent extends ALevelContent {

  // renders this BlankGroundContent as an image
  public WorldImage renderPiece() {
    return new FromFileImage("blank.png");
  }

  // this is can hold a player
  public boolean canHoldPlayer() {
    return true;
  }
}

// represents a trophy, a piece of level content, that is movable and is of a given color
class Trophy extends ALevelContent {
  String color;

  Trophy(String color) {
    this.color = color;
  }

  // renders this Trophy as an image
  public WorldImage renderPiece() {
    if (this.color.equals("green")) {
      return new FromFileImage("green_trophy.png");
    }
    else if (this.color.equals("red")) {
      return new FromFileImage("red_trophy.png");
    }
    else if (this.color.equals("blue")) {
      return new FromFileImage("blue_trophy.png");
    }
    else if (this.color.equals("yellow")) {
      return new FromFileImage("yellow_trophy.png");
    }
    else {
      throw new RuntimeException("Cannot render!");
    }
  }

  // Returns true if the trophy is the wrong color and false if the colors match
  public boolean wrongColor(String color) {
    return !(this.color.equals(color));
  }

  // A trophy can be pushed by the player
  public boolean canBePushed() {
    return true;
  }
}

// represents a box, a piece of level content, that is movable
class Box extends ALevelContent {

  // renders this Box as an image
  public WorldImage renderPiece() {
    return new FromFileImage("box.png");
  }

  // A box an be pushed by a player
  public boolean canBePushed() {
    return true;
  }
}

// represents a wall, a piece of ground content, that is not movable
class Wall extends ALevelContent {

  // renders this Wall as an image
  public WorldImage renderPiece() {
    return new FromFileImage("wall.png");
  }
}

// represents a player, a piece of level content, that is movable in the given direction
class Player extends ALevelContent {

  // renders this Player as an image
  public WorldImage renderPiece() {
    return new FromFileImage("player_right.png");
  }

  // this is movable on key press
  public boolean movableOnKey() {
    return true;
  }
}

// represents a hole, a piece of level content, that is not movable and 
// can disappear if a player or movable content is pushed into it
class Hole extends ALevelContent {

  // renders this Hole as an image
  public WorldImage renderPiece() {
    return new FromFileImage("hole.png");
  }

  // A hole can cause content pieces to be lost
  public boolean canBeLost() {
    return true;
  }
}

// represents a unit of the board containing a piece of ground content and a piece of level 
// content and an x and y coordinate (representing the location of this cell)
class Cell {
  int y;
  int x;
  IGroundContent g;
  ILevelContent l;

  Cell(int y, int x, IGroundContent g, ILevelContent l) {
    this.y = y;
    this.x = x;
    this.g = g;
    this.l = l;
  }

  // returns true if this cell contains the player
  public boolean locatePlayer() {
    return this.l.movableOnKey();
  }

  // creates a new cell with this cell's contents edited
  public Cell editCellBlank() {
    return new Cell(this.y, this.x, this.g, this.l.changeToBlank());
  }

  // creates a new cell with this cell's contents edited
  public Cell editCellCompleteBlank() {
    return new Cell(this.y, this.x, new BlankGroundContent(), this.l.changeToBlank());
  }

  // creates a new cell with this cell's contents edited
  public Cell editCellGiven(Cell given) {
    return new Cell(this.y, this.x, this.g, this.l.changeToGiven(given.l));
  }

  // creates a new cell with this cell's contents edited
  public Cell editCellGivenBlankGround(Cell given) {
    if (this.cellCanSlide()) {
      return new Cell(this.y, this.x, new BlankGroundContent(), this.l.changeToGiven(given.l));
    }
    else {
      return new Cell(this.y, this.x, this.g, this.l.changeToGiven(given.l));
    }
  }

  // determines if this cells level content can be pushed by the player
  public boolean cellCanBePushed() {
    return l.canBePushed();
  }

  // determines if this cells level content can hold a player
  public boolean cellCanHoldPlayer() {
    return l.canHoldPlayer();
  }

  // determines if this cells level content can cause other movable content to be
  // lost
  public boolean cellCanBeLost() {
    return l.canBeLost();
  }

  // determines if this cells ground content can cause a player or other movable
  // content to slide
  public boolean cellCanSlide() {
    return g.canSlide();
  }

  // determines if this cell has ice and a movable piece on top
  public boolean iceWithMovable() {
    return l.canBePushed() && g.canSlide();
  }
}

// represents a level in the game Sokoban

// INVARIANT: 
class Level {
  int height;
  int width;
  ArrayList<ArrayList<Cell>> board;
  Posn playerPos;

  // main constructor that initializes the level with a given board (which is an
  // ArrayList<ArrayList<Cell>>)
  Level(int height, int width, ArrayList<ArrayList<Cell>> board, Posn playerPos) {
    this.height = height;
    this.width = width;
    this.board = board;
    this.playerPos = playerPos;
  }

  // other constructor that takes in two level-description strings (one
  // representing ground content and the other representing the level content) and
  // populates the board
  Level(String groundCont, String levelCont) {
    this.height = new Utils().getHeight(groundCont);
    this.width = new Utils().getWidth(groundCont);
    this.board = new Utils().configureBoard(groundCont, levelCont, 0, groundCont.indexOf("\n"));
    this.playerPos = new Posn(getPlayerInBoard().x, getPlayerInBoard().y);
  }

  // returns true if every target has a trophy on top of it, whose color matches
  // the target’s color
  public boolean levelWon() {
    // this nested for loop is used to iterate through the board. The outer loop
    // iterates over the rows. The inner loop iterates within the rows and checks if
    // each cell contains a target without a matching trophy. If the condition is
    // true, then the game has not been won and the method returns false.
    // The loop correctly iterates using the board's height and width.
    for (int i = 0; i <= this.height; i += 1) {
      for (int j = 0; j <= this.width; j += 1) {
        Cell curr = this.board.get(i).get(j);
        // returns false if the current cell's ground content is an empty target or the
        // trophy color doesn't match the target color
        if (curr.g.emptyTarget(curr.l)) {
          return false;
        }
      }
    }
    // returns true if we iterated through and there are no targets without trophies
    // of a matching color
    return true;
  }

  // renders the level as a WorldImage
  public WorldImage render() {
    WorldImage finalImage = new EmptyImage();
    // this nested for loop is used to iterate through the board. The outer loop
    // iterates over the rows and places the row images above one another. The inner
    // loop iterates within the rows and places the cells (converted to images)
    // beside one another. The loop correctly iterates using the board's height and
    // width.

    for (int i = 0; i <= this.height; i += 1) {
      WorldImage rowImage = new EmptyImage();
      for (int j = 0; j <= this.width; j += 1) {
        rowImage = new BesideImage(rowImage,
            new Utils().cellToWorldImage(this.board.get(i).get(j)));
      }
      finalImage = new AboveImage(finalImage, rowImage);
    }

    return finalImage;
  }

  // returns a new level with this height and this width and an edited board
  // according to the horizontal move
  public Level newLevelHorizontal(int dy, int dx) {

    int playerY = this.playerPos.y;
    int playerX = this.playerPos.x;

    return this.editBoardHorizontal(playerY, playerX, dy, dx);
  }

  // returns a new level with this height and this width and an edited board
  // according to the vertical move
  public Level newLevelVertical(int dy, int dx) {

    int playerY = this.playerPos.y;
    int playerX = this.playerPos.x;

    return this.editBoardVertical(playerY, playerX, dy, dx);
  }

  public ArrayList<ArrayList<Cell>> moveToBlankHorizontal(ArrayList<ArrayList<Cell>> newBoard,
      int y, int x, int dy, int dx) {

    ArrayList<Cell> newCurrRow = newBoard.get(y);

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayHorizontal = newBoard.get(y).get(x + dx);

    Cell newCurr = curr.editCellBlank();
    Cell newOneAway = oneAwayHorizontal.editCellGiven(curr);

    newCurrRow.set(x, newCurr);
    newCurrRow.set(x + dx, newOneAway);
    newBoard.set(y, newCurrRow);

    return newBoard;
  }

  public ArrayList<ArrayList<Cell>> pushHorizontal(ArrayList<ArrayList<Cell>> newBoard, int y,
      int x, int dy, int dx) {

    ArrayList<Cell> newCurrRow = newBoard.get(y);

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayHorizontal = newBoard.get(y).get(x + dx);
    Cell twoAwayHorizontal = newBoard.get(y).get(x + (2 * dx));

    Cell newCurr = curr.editCellBlank();
    Cell newOneAway = oneAwayHorizontal.editCellGiven(curr);
    Cell newTwoAway = twoAwayHorizontal.editCellGiven(oneAwayHorizontal);

    newCurrRow.set(x, newCurr);
    newCurrRow.set(x + dx, newOneAway);
    newCurrRow.set(x + (2 * dx), newTwoAway);
    newBoard.set(y, newCurrRow);

    return newBoard;
  }

  public ArrayList<ArrayList<Cell>> moveToBlankVertical(ArrayList<ArrayList<Cell>> newBoard, int y,
      int x, int dy, int dx) {

    ArrayList<Cell> newCurrRow = newBoard.get(y);
    ArrayList<Cell> newRowOneAway = newBoard.get(y + dy);

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayVertical = newBoard.get(y + dy).get(x);

    Cell newCurr = curr.editCellBlank();
    Cell newOneAway = oneAwayVertical.editCellGiven(curr);

    newCurrRow.set(x, newCurr);
    newRowOneAway.set(x, newOneAway);
    newBoard.set(y, newCurrRow);
    newBoard.set(y + dy, newRowOneAway);

    return newBoard;
  }

  public ArrayList<ArrayList<Cell>> pushVertical(ArrayList<ArrayList<Cell>> newBoard, int y, int x,
      int dy, int dx) {

    ArrayList<Cell> newCurrRow = newBoard.get(y);
    ArrayList<Cell> newRowOneAway = newBoard.get(y + dy);
    ArrayList<Cell> newRowTwoAway = newBoard.get(y + (2 * dy));

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayVertical = newBoard.get(y + dy).get(x);
    Cell twoAwayVertical = newBoard.get(y + (2 * dy)).get(x);

    Cell newCurr = curr.editCellBlank();
    Cell newOneAway = oneAwayVertical.editCellGiven(curr);
    Cell newTwoAway = twoAwayVertical.editCellGiven(oneAwayVertical);

    newCurrRow.set(x, newCurr);
    newRowOneAway.set(x, newOneAway);
    newRowTwoAway.set(x, newTwoAway);
    newBoard.set(y, newCurrRow);
    newBoard.set(y + dy, newRowOneAway);
    newBoard.set(y + (2 * dy), newRowTwoAway);

    return newBoard;
  }

  // Creates a new level with correctly updated cells based on the current board
  // and the dy and dx which moves the player horizontally on the board
  public Level editBoardHorizontal(int y, int x, int dy, int dx) {

    if (y + (2 * dy) > this.height || x + (2 * dx) > this.width || x + (2 * dx) < 0
        || y + (2 * dy) < 0) {
      return this;
    }

    int w = this.width;
    int h = this.height;
    Posn playerPosn = this.playerPos;

    ArrayList<ArrayList<Cell>> newBoard = new ArrayList<ArrayList<Cell>>();

    for (ArrayList<Cell> AL : this.board) {
      ArrayList<Cell> newRow = new ArrayList<Cell>();
      for (Cell c : AL) {
        Cell newCell = new Cell(c.y, c.x, c.g, c.l);
        newRow.add(newCell);
      }
      newBoard.add(newRow);
    }

    ArrayList<Cell> newCurrRow = newBoard.get(y);

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayHorizontal = newBoard.get(y).get(x + dx);
    Cell twoAwayHorizontal = newBoard.get(y).get(x + (2 * dx));

    // if the adjacent cell is ice
    if (oneAwayHorizontal.cellCanSlide() && oneAwayHorizontal.cellCanHoldPlayer()) {

      newBoard = this.moveToBlankHorizontal(newBoard, y, x, dy, dx);
      Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

      return tempLevel.newLevelHorizontal(dy, dx);

    }
    // if the adjacent cell is ice and the next cell is blank
    else if (oneAwayHorizontal.iceWithMovable() && twoAwayHorizontal.cellCanHoldPlayer()) {

      newBoard = this.pushHorizontal(newBoard, y, x, dy, dx);
      Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

      return tempLevel.newLevelHorizontal(dy, dx);

    }
    // if the adjacent cell can be pushed and the next cell is ice
    else if (oneAwayHorizontal.cellCanBePushed() && twoAwayHorizontal.cellCanSlide()
        && twoAwayHorizontal.cellCanHoldPlayer()) {

      int factor = this.countIceHorizontal(newCurrRow, dx) + 1;
      Cell afterIce = newBoard.get(y).get(x + dx + (dx * factor));

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayHorizontal.editCellGiven(curr);

      newCurrRow.set(x, newCurr);
      newCurrRow.set(x + dx, newOneAway);

      if (afterIce.cellCanHoldPlayer() && !this.iceWithMovableHorizontal(newCurrRow, dx)) {

        Cell newAfterIce = afterIce.editCellGivenBlankGround(oneAwayHorizontal);
        newCurrRow.set(x + dx + (dx * factor), newAfterIce);
        newBoard.set(y, newCurrRow);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }
      // if the adjacent cell is ice with a pushable object on it
      else if (this.iceWithMovableHorizontal(newCurrRow, dx)) {

        Cell newTwoAway = twoAwayHorizontal.editCellGiven(oneAwayHorizontal);
        newCurrRow.set(x + (2 * dx), newTwoAway);
        newBoard.set(y, newCurrRow);

        Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

        return tempLevel.newLevelHorizontal(dy, dx);
      }
      else if (afterIce.cellCanBeLost() && !this.iceWithMovableHorizontal(newCurrRow, dx)) {

        Cell newAfterIce = afterIce.editCellCompleteBlank();
        newCurrRow.set(x + dx + (dx * factor), newAfterIce);
        newBoard.set(y, newCurrRow);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }
      else {

        Cell afterIceAlt = newBoard.get(y).get(x + (dx * factor));
        Cell newAfterIce = afterIceAlt.editCellGiven(oneAwayHorizontal);
        newCurrRow.set(x + (dx * factor), newAfterIce);
        newBoard.set(y, newCurrRow);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }
    }
    // if the adjacent cell is blank
    else if (oneAwayHorizontal.cellCanHoldPlayer()) {

      newBoard = this.moveToBlankHorizontal(newBoard, y, x, dy, dx);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    // if the adjacent cell is a content that can be pushed and the following cell
    // is blank
    else if (oneAwayHorizontal.cellCanBePushed() && twoAwayHorizontal.cellCanHoldPlayer()) {

      newBoard = this.pushHorizontal(newBoard, y, x, dy, dx);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    // if the adjacent cell is a hole
    else if (oneAwayHorizontal.cellCanBeLost()) {

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayHorizontal.editCellBlank();

      newCurrRow.set(x, newCurr);
      newCurrRow.set(x + dx, newOneAway);
      newBoard.set(y, newCurrRow);

      return new Level(h, w, newBoard, new Posn(-1, -1));
    }
    // if the adjacent cell is a movable content and the following cell is a hole
    else if (oneAwayHorizontal.cellCanBePushed() && twoAwayHorizontal.cellCanBeLost()) {

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayHorizontal.editCellGiven(curr);
      Cell newTwoAway = twoAwayHorizontal.editCellBlank();

      newCurrRow.set(x, newCurr);
      newCurrRow.set(x + dx, newOneAway);
      newCurrRow.set(x + (2 * dx), newTwoAway);
      newBoard.set(y, newCurrRow);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    else {
      return this;
    }
  }

  // Creates a new level with correctly updated cells based on the current board
  // and the dy and dx which moves the player vertically on the board
  public Level editBoardVertical(int y, int x, int dy, int dx) {

    int w = this.width;
    int h = this.height;
    Posn playerPosn = this.playerPos;

    // condition to ensure that you cannot move the player out of bounds
    if (y + (2 * dy) > this.height || x + (2 * dx) > this.width || x + (2 * dx) < 0
        || y + (2 * dy) < 0) {
      return this;
    }

    // avoid aliasing
    ArrayList<ArrayList<Cell>> newBoard = new ArrayList<ArrayList<Cell>>();

    for (ArrayList<Cell> AL : this.board) {
      ArrayList<Cell> newRow = new ArrayList<Cell>();
      for (Cell c : AL) {
        Cell newCell = new Cell(c.y, c.x, c.g, c.l);
        newRow.add(newCell);
      }
      newBoard.add(newRow);
    }

    ArrayList<Cell> newCurrRow = newBoard.get(y);
    ArrayList<Cell> newRowOneAway = newBoard.get(y + dy);
    ArrayList<Cell> newRowTwoAway = newBoard.get(y + (2 * dy));

    Cell curr = newBoard.get(y).get(x);
    Cell oneAwayVertical = newBoard.get(y + dy).get(x);
    Cell twoAwayVertical = newBoard.get(y + (2 * dy)).get(x);

    if (oneAwayVertical.cellCanSlide() && oneAwayVertical.cellCanHoldPlayer()) {

      newBoard = this.moveToBlankVertical(newBoard, y, x, dy, dx);
      Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

      return tempLevel.newLevelVertical(dy, dx);

    }
    else if (oneAwayVertical.iceWithMovable() && twoAwayVertical.cellCanHoldPlayer()) {

      newBoard = this.pushVertical(newBoard, y, x, dy, dx);
      Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

      return tempLevel.newLevelVertical(dy, dx);
    }
    else if (oneAwayVertical.cellCanBePushed() && twoAwayVertical.cellCanSlide()
        && twoAwayVertical.cellCanHoldPlayer()) {

      int factor = this.countIceVertical(newBoard, dy) + 1;
      ArrayList<Cell> afterIceRow = newBoard.get(y + dy + (dy * factor));
      Cell afterIce = newBoard.get(y + dy + (dy * factor)).get(x);

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayVertical.editCellGiven(curr);
      newCurrRow.set(x, newCurr);
      newRowOneAway.set(x, newOneAway);
      newBoard.set(y, newCurrRow);
      newBoard.set(y + dy, newRowOneAway);

      if (afterIce.cellCanHoldPlayer()) {

        Cell newAfterIce = afterIce.editCellGivenBlankGround(oneAwayVertical);
        afterIceRow.set(x, newAfterIce);
        newBoard.set(y + dy + (dy * factor), afterIceRow);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }
      else if (afterIce.cellCanBeLost() && !this.iceWithMovableVertical(newBoard, dy)) {

        Cell newAfterIce = afterIce.editCellBlank();
        afterIceRow.set(x, newAfterIce);
        newBoard.set(y + dy + (dy * factor), afterIceRow);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }

      else if (this.iceWithMovableVertical(newBoard, dy)) {

        Cell newTwoAway = twoAwayVertical.editCellGiven(oneAwayVertical);
        newRowTwoAway.set(x, newTwoAway);
        newBoard.set(y + (2 * dy), newRowTwoAway);
        Level tempLevel = new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));

        return tempLevel.newLevelHorizontal(dy, dx);
      }
      else {

        ArrayList<Cell> afterIceRowAlt = newBoard.get(y + (dy * factor));
        Cell afterIceAlt = newBoard.get(y + (dy * factor)).get(x);

        Cell newAfterIce = afterIceAlt.editCellGiven(oneAwayVertical);
        afterIceRowAlt.set(x, newAfterIce);
        newBoard.set(y + (dy * factor), afterIceRowAlt);

        return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
      }
    }

    else if (oneAwayVertical.cellCanHoldPlayer()) {

      newBoard = this.moveToBlankVertical(newBoard, y, x, dy, dx);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    // if the adjacent cell is a movable content piece and the following cell is
    // blank
    else if (oneAwayVertical.cellCanBePushed() && twoAwayVertical.cellCanHoldPlayer()) {

      newBoard = this.pushVertical(newBoard, y, x, dy, dx);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    // if the adjacent cell is a hole
    else if (oneAwayVertical.cellCanBeLost()) {

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayVertical.editCellBlank();

      newCurrRow.set(x, newCurr);
      newRowOneAway.set(x, newOneAway);
      newBoard.set(y, newCurrRow);
      newBoard.set(y + dy, newRowOneAway);

      return new Level(h, w, newBoard, new Posn(-1, -1));
    }
    // if the adjacent cell is a movable content piece and the following cell is a
    // hole
    else if (oneAwayVertical.cellCanBePushed() && twoAwayVertical.cellCanBeLost()) {

      Cell newCurr = curr.editCellBlank();
      Cell newOneAway = oneAwayVertical.editCellGiven(curr);
      Cell newTwoAway = twoAwayVertical.editCellBlank();

      newCurrRow.set(x, newCurr);
      newRowOneAway.set(x, newOneAway);
      newRowTwoAway.set(x, newTwoAway);
      newBoard.set(y, newCurrRow);
      newBoard.set(y + dy, newRowOneAway);
      newBoard.set(y + (2 * dy), newRowTwoAway);

      return new Level(h, w, newBoard, new Posn(playerPosn.x + dx, playerPosn.y + dy));
    }
    else {

      return this;
    }
  }

  // returns the cell containing the player in this level's board
  // if there is no player it returns a blank cell with coordinates (-1, -1)
  // this method is only called at level instantiation, to set the player's coord
  public Cell getPlayerInBoard() {
    for (int i = 0; i <= this.height; i += 1) {
      for (int j = 0; j <= this.width; j += 1) {
        if (this.board.get(i).get(j).locatePlayer()) {
          return this.board.get(i).get(j);
        }
      }
    }
    return new Cell(-1, -1, new BlankGroundContent(), new BlankLevelContent());
  }

  // determines if the world should end which occurs either when the player has
  // gone into a hole (and therefore is placed in an invalid cell)
  // or if the level has been won (all targets have the matching color trophy on
  // them)
  public boolean shouldEnd() {
    return ((this.playerPos.x == -1 && this.playerPos.y == -1) || this.levelWon());
  }

  // counts the number of consecutive ice pieces in a horizontal row that a level
  // content can slide on
  public int countIceHorizontal(ArrayList<Cell> row, int dx) {
    int sumSoFar = 0;

    for (int index = this.playerPos.x + 2 * dx; index < row.size() && index >= 0; index += 1 * dx) {
      if (row.get(index).cellCanSlide()) {
        sumSoFar += 1;
      }
      else {
        return sumSoFar;
      }
    }
    return sumSoFar;
  }

  // counts the number of consecutive ice pieces in a vertical row that a level
  // content can slide on
  public int countIceVertical(ArrayList<ArrayList<Cell>> board, int dy) {
    int sumSoFar = 0;

    for (int index = this.playerPos.y + (2 * dy); index < board.size()
        && index >= 0; index += 1 * dy) {
      if (board.get(index).get(this.playerPos.x).cellCanSlide()) {
        sumSoFar += 1;
      }
      else {
        return sumSoFar;
      }
    }
    return sumSoFar;
  }

  // determines if there is any ice in the horizontal row with a movable object on
  // top of it
  public boolean iceWithMovableHorizontal(ArrayList<Cell> row, int dx) {
    for (int i = this.playerPos.x + (2 * dx); i < row.size() && i >= 0; i += (1 * dx)) {
      if (row.get(i).iceWithMovable()) {
        return true;
      }
    }
    return false;
  }

  // determines if there is any ice in the vertical row with a movable object on
  // top of it
  public boolean iceWithMovableVertical(ArrayList<ArrayList<Cell>> board, int dy) {
    for (int i = this.playerPos.y + (2 * dy); i < board.size() && i >= 0; i += 1 * dy) {
      if (board.get(i).get(this.playerPos.x).iceWithMovable()) {
        return true;
      }
    }
    return false;
  }
}

// represents a User-interface World where a user can play Sokoban
class SokobanWorld extends World {

  Level curr;
  SokobanWorld initial;
  SokobanWorld prevWorld;
  int score;

  SokobanWorld(Level curr, SokobanWorld initial, SokobanWorld prevWorld, int score) {
    this.curr = curr;
    this.initial = initial;
    this.prevWorld = prevWorld;
    this.score = score;
  }

  SokobanWorld(Level curr) {
    this.curr = curr;
    this.initial = this;
    this.prevWorld = this;
    this.score = 0;
  }

  // renders the score as an image
  public WorldImage drawScore() {
    WorldImage backgroundScore = new RectangleImage(50, 35, OutlineMode.SOLID, Color.WHITE);
    WorldImage backgroundLabel = new RectangleImage(50, 15, OutlineMode.SOLID, Color.WHITE);
    WorldImage score = new TextImage(Integer.toString(this.score), 24, Color.BLUE);
    WorldImage label = new TextImage("SCORE:", 16, Color.BLUE);
    return new AboveImage(new OverlayImage(label, backgroundLabel),
        new OverlayImage(score, backgroundScore));
  }

  // makes the scene with the game level rendered
  public WorldScene makeScene() {
    return new WorldScene(500, 500).placeImageXY(this.curr.render(), 250, 250)
        .placeImageXY(this.drawScore(), 30, 35);
  }

  // checks if the player has moved into a hole or if the level is won
  public boolean shouldEnd() {
    return this.curr.shouldEnd();
  }

  // scene showing that the game ends
  public WorldScene lastScene(String msg) {
    return new WorldScene(500, 500).placeImageXY(
        new AboveImage(new TextImage(msg, 24, FontStyle.BOLD, Color.RED),
            new TextImage("Final Score: " + Integer.toString(this.score), 24, Color.BLUE)),
        250, 250);
  }

  // returns a new SokobanWorld with a player moved in the given key direction
  // a key press corresponds to a coordinate (int dy, int dx)
  // the new SokobanWorld is set with the initial level edited according to the
  // move made
  public SokobanWorld onKeyEvent(String key) {
    // moving up means the player's y position is one less than it was before and
    // the x position does not change.
    // ex: if the player was in cell (2, 2), then after the move, the player is in
    // cell (1, 2)
    // therefore, (dy, dx) = (-1, 0)
    if (key.equals("up")) {
      return new SokobanWorld(this.curr.newLevelVertical(-1, 0), this.initial, this,
          this.score + 1);
    }
    // moving down means the player's y position is one more than it was before and
    // the x position does not change.
    // ex: if the player was in cell (2, 2), then after the move, the player is in
    // cell (3, 2)
    // therefore, (dy, dx) = (1, 0)
    else if (key.equals("down")) {
      return new SokobanWorld(this.curr.newLevelVertical(1, 0), this.initial, this, this.score + 1);
    }
    // moving left means the player's y position does not change and the x position
    // is one less than it was before.
    // ex: if the player was in cell (2, 2), then after the move, the player is in
    // cell (2, 1)
    // therefore, (dy, dx) = (0, -1)
    else if (key.equals("left")) {
      return new SokobanWorld(this.curr.newLevelHorizontal(0, -1), this.initial, this,
          this.score + 1);
    }

    // moving left means the player's y position does not change and the x position
    // is one more than it was before.
    // ex: if the player was in cell (2, 2), then after the move, the player is in
    // cell (2, 3)
    // therefore, (dy, dx) = (0, 1)
    else if (key.equals("right")) {
      return new SokobanWorld(this.curr.newLevelHorizontal(0, 1), this.initial, this,
          this.score + 1);
    }
    // pressing "u" allows the player to undo their previous move one time and so
    // pressing "u"
    // restores the previous state
    else if (key.equals("u")) {
      if (this == this.initial) {
        return this;
      }
      else {
        SokobanWorld prev = this.prevWorld;
        prev.score = this.score + 1;

        return prev;
      }
    }
    
    // there is no movement and therefore, no reason to change the Sokoban world
    else {
      return this;
    }
  }
 
  // 
  public World onTick() {
    if (this.shouldEnd()) {
      return this.endOfWorld("Game Over");
    }
    else {
      return this;
    }
  }
}

// Utils Class 
class Utils {

  // converts a string to the IGroundContent that the string represents
  public IGroundContent charToGroundContent(String s) {
    if (s.equals("Y")) {
      return new Target("yellow");
    }
    else if (s.equals("G")) {
      return new Target("green");
    }
    else if (s.equals("B")) {
      return new Target("blue");
    }
    else if (s.equals("R")) {
      return new Target("red");
    }
    else if (s.equals("_")) {
      return new BlankGroundContent();
    }
    else if (s.equals("I")) {
      return new Ice();
    }
    else {
      throw new IllegalArgumentException("must only construct a GroundContent Piece");
    }
  }

  // converts a string to the ILevelContent that the string represents
  public ILevelContent charToLevelContent(String s) {
    if (s.equals("y")) {
      return new Trophy("yellow");
    }
    else if (s.equals("g")) {
      return new Trophy("green");
    }
    else if (s.equals("b")) {
      return new Trophy("blue");
    }
    else if (s.equals("r")) {
      return new Trophy("red");
    }
    else if (s.equals(">")) {
      return new Player();
    }
    else if (s.equals("<")) {
      return new Player();
    }
    else if (s.equals("^")) {
      return new Player();
    }
    else if (s.equals("v")) {
      return new Player();
    }
    else if (s.equals("W")) {
      return new Wall();
    }
    else if (s.equals("_")) {
      return new BlankLevelContent();
    }
    else if (s.equals("B")) {
      return new Box();
    }
    else if (s.equals("h")) {
      return new Hole();
    }
    else {
      throw new IllegalArgumentException("must only construct a LevelContent Piece");
    }
  }

  // takes in two strings representing a row of each the level description
  // strings. creates a new cell from the first character of both strings with the
  // given y and the local x position by calling the respective methods that
  // convert strings
  // to pieces and looping over the conversion with the "rest" of
  // the strings (the strings excluding the first character). Increments the x
  // since the every iterator call means a new column is started.
  public ArrayList<Cell> stringsToPiecesToCell(String s1, String s2, int y) {

    ArrayList<Cell> row = new ArrayList<>();

    int x = 0;
    // this loop continues to convert the given strings into cells by single
    // substrings until all
    // strings have been processed.
    while (s1.length() != 0 && s2.length() != 0) {
      row.add(new Cell(y, x, charToGroundContent(s1.substring(0, 1)),
          charToLevelContent(s2.substring(0, 1))));
      s1 = s1.substring(1);
      s2 = s2.substring(1);
      x += 1;
    }
    return row;
  }

  // takes in the two level description string, two ints (representing some height
  // and some width), and the exact width of the board.
  // each description string is split to return a string representing a row
  // of the game and and stringsToPiecesToCell is called to convert
  // the string representing a row into an ArrayList<Cell> representing the row.
  // the method is recursively called on the "rest" of both description strings
  // with the y being incremented as the iterative call with create a new row.
  public ArrayList<ArrayList<Cell>> configureBoard(String groundStuff, String levelStuff, int y,
      int width) {

    if (groundStuff.length() != levelStuff.length()) {
      throw new IllegalArgumentException("given strings do not match in length");
    }

    ArrayList<ArrayList<Cell>> board = new ArrayList<>();
    // this loop continues to split up the given strings from the beginning of the
    // string to "\n"; that resulting string
    // represents the row. stringsToPiecesToCell, which transforms a
    // row-representing string into an AL<Cells>, is then called and
    // the result is added as a row to the board. Then, since we begin a new row
    // after, the y-value is incremented and both given
    // strings are changed to be all characters after the characters representing
    // the row that has been transformed.
    while (groundStuff.contains("\n") && levelStuff.contains("\n")) {
      String groundRow = split(groundStuff, width);
      String levelRow = split(levelStuff, width);
      ArrayList<Cell> row = stringsToPiecesToCell(groundRow, levelRow, y);
      board.add(row);
      groundStuff = groundStuff.substring(width + 1);
      levelStuff = levelStuff.substring(width + 1);
      y += 1;
    }
    // adds the last row to board
    if (!groundStuff.isEmpty() && !levelStuff.isEmpty()) {
      ArrayList<Cell> row = stringsToPiecesToCell(groundStuff, levelStuff, y);
      board.add(row);
    }

    return board;
  }

  // splits the given string by returning the substring from the beginning of the
  // string to a given length (exclusive)
  public String split(String s, int length) {
    return s.substring(0, length);
  }

  // returns the height of a level description string by recursively counting the
  // number of
  // new lines
  public int getHeight(String s) {
    if (s.length() == 0) {
      return 0;
    }
    else if (s.charAt(0) == '\n') {
      return 1 + getHeight(s.substring(1));
    }
    else {
      return getHeight(s.substring(1));
    }
  }

  // returns the width of a level description string by finding index of \n
  // otherwise just returns s.length
  public int getWidth(String s) {
    if (s.indexOf("\n") == -1) {
      return s.length() - 1;
    }
    else {
      return s.indexOf("\n") - 1;
    }
  }

  // renders a cell as an image by overlaying the given cell's rendered
  // levelContent on the given cell's rendered groundContent
  public WorldImage cellToWorldImage(Cell c) {
    return new OverlayImage(c.l.renderPiece(), c.g.renderPiece());
  }
}

class ExamplesSokoban {

  // ---------- EXAMPLE BOARDS/LEVELS ---------- //

  // given homework example of a Sokoban Level
  String exampleLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
      + "________\n" + "___G____\n" + "________";
  String exampleLevelContents = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "W_b>yB_W\n"
      + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";
  Level testLevel = new Level(exampleLevelGround, exampleLevelContents);

  // 2x2 : small example Sokoban board
  String twoByTwoGroundCont = "__\n__";
  String twoByTwoLevelCont = "_W\n_W";
  Level twoByTwo = new Level(this.twoByTwoGroundCont, this.twoByTwoLevelCont);

  boolean testTwoByTwoBoardConstructor(Tester t) {
    ArrayList<Cell> r1 = new ArrayList<>();
    ArrayList<Cell> r2 = new ArrayList<>();
    ArrayList<ArrayList<Cell>> board = new ArrayList<>();
    r1.add(new Cell(0, 0, new BlankGroundContent(), new BlankLevelContent()));
    r1.add(new Cell(0, 1, new BlankGroundContent(), new Wall()));
    r2.add(new Cell(1, 0, new BlankGroundContent(), new BlankLevelContent()));
    r2.add(new Cell(1, 1, new BlankGroundContent(), new Wall()));
    board.add(r1);
    board.add(r2);
    return t.checkExpect(twoByTwo.board, board);
  }

  Level testLevel3 = new Level(
      "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "_>W_WW__\n" + "WWWr_WWW\n" + "W_b_yB_W\n" + "WW_gWWWW\n" + "_WW_W___\n"
          + "__WWW___");

  Level testLevelWon = new Level(
      "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "__WrWW__\n" + "WWW__WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
          + "__WWW___");

  Level testLevelAlmostWon = new Level(
      "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
          + "__WWW___");

  Level moreTarThanTrop = new Level(
      "________\n" + "___R____\n" + "____R___\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
          + "__WWW___");

  Level moreTrophThanTar = new Level(
      "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "__WrWWr_\n" + "WWW__WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
          + "__WWW___");

  Level wrongColors = new Level(
      "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
          + "________",
      "__WWW___\n" + "__WbWW__\n" + "WWW__WWW\n" + "Wy_>_BgW\n" + "WW__WWWW\n" + "_WWrW___\n"
          + "__WWW___");

  Level noPlayer = new Level("_B\n" + "R_\n" + "__", "W_\n" + "_W\n" + "__");
  Level justPlayer = new Level(
      "________\n" + "________\n" + "________\n" + "________\n" + "________\n" + "________\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
          + "WWWWWWWW");

  Level withHole = new Level(
      "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
          + "_______\n" + "_______",
      "WWWWWWW\n" + "W_>___W\n" + "W_h___W\n" + "Wh_hr_W\n" + "W_h___W\n" + "W_____W\n"
          + "W_____W\n" + "WWWWWWW");

  Level withIce = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
      "_WWWWWWW\n" + "W______W\n" + "W>______\n" + "WW___WWW\n" + "_WWWWW__");

  Level withIceBig = new Level(
      "________\n" + "___Y____\n" + "___I_I__\n" + "___I____\n" + "_R_III__\n" + "______B_\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W___Br_W\n" + "W______W\n" + "W_>____W\n" + "W______W\n"
          + "WWWWWWWW");
  Level withIceTrophy = new Level(
      "________\n" + "___Y____\n" + "___I_I__\n" + "___I____\n" + "_R_IIII_\n" + "______B_\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W___Br_W\n" + "W______W\n" + "W>y____W\n" + "W______W\n"
          + "WWWWWWWW");
  Level withIceTrophy2 = new Level(
      "________\n" + "___Y____\n" + "___I_I__\n" + "___I____\n" + "_R_III__\n" + "______B_\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W___Br_W\n" + "W______W\n" + "W>y__b_W\n" + "W______W\n"
          + "WWWWWWWW");
  Level withSlipNSlide = new Level(
      "________\n" + "___Y____\n" + "___I____\n" + "___I_I__\n" + "_R_III__\n" + "________\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W___Br_W\n" + "W______W\n" + "W>y____W\n" + "W____h_W\n"
          + "WWWWWWWW");
  Level withVerticalIce = new Level(
      "________\n" + "__IY____\n" + "__II_I__\n" + "__II____\n" + "_R_III__\n" + "__I___B_\n"
          + "________",
      "WWWWWWWW\n" + "W______W\n" + "W_y_Br_W\n" + "W______W\n" + "W_>____W\n" + "W______W\n"
          + "WWWWWWWW");

  ILevelContent testTrophyRed = new Trophy("red");

  ILevelContent testTrophyBlue = new Trophy("blue");

  ILevelContent testBlankContent = new BlankLevelContent();

  IGroundContent testTargetRed = new Target("red");

  // ---------- TESTS FOR BOARD CREATION W/ HELPERS ---------- //

  boolean testSplit1(Tester t) {
    return t.checkExpect(
        new Utils().split(this.twoByTwoGroundCont, this.twoByTwoGroundCont.indexOf("\n")), "__");
  }

  boolean testSplit2(Tester t) {
    return t.checkExpect(new Utils().split("abcd", 2), "ab");
  }

  boolean testSplit3(Tester t) {
    return t.checkExpect(new Utils().split("___R____\n", "___R____\n".indexOf("\n")), "___R____");
  }

  boolean testGetHeight1(Tester t) {
    return t.checkExpect(new Utils().getHeight(twoByTwoGroundCont), 1);
  }

  boolean testGetHeight2(Tester t) {
    return t.checkExpect(new Utils().getHeight("_____\n___\n ___\n___"), 3);
  }

  boolean testCharToGroundCont1(Tester t) {
    return t.checkExpect(new Utils().charToGroundContent("_"), new BlankGroundContent());
  }

  boolean testCharToGroundCont2(Tester t) {
    return t.checkExpect(new Utils().charToGroundContent("R"), new Target("red"));
  }

  boolean testCharToGroundCont3(Tester t) {
    return t.checkException(
        new IllegalArgumentException("must only construct a GroundContent Piece"), new Utils(),
        "charToGroundContent", "x");
  }

  boolean testCharToLvlCont1(Tester t) {
    return t.checkExpect(new Utils().charToLevelContent("_"), new BlankLevelContent());
  }

  boolean testCharToLvlCont2(Tester t) {
    return t.checkExpect(new Utils().charToLevelContent("r"), new Trophy("red"));
  }

  boolean testCharToLvlCont3(Tester t) {
    return t.checkExpect(new Utils().charToLevelContent(">"), new Player());
  }

  boolean testCharToLvlCont4(Tester t) {
    return t.checkException(
        new IllegalArgumentException("must only construct a LevelContent Piece"), new Utils(),
        "charToLevelContent", "x");
  }

  // ---------- TESTS FOR LEVELWON ---------- //

  boolean testLevelWon(Tester t) {
    // test a level that has won
    return t.checkExpect(testLevelWon.levelWon(), true)

        // test a level that has not won (no trophies on targets)
        && t.checkExpect(testLevel.levelWon(), false)

        // test a level with all but one trophy correct
        && t.checkExpect(testLevelAlmostWon.levelWon(), false)

        // test a level with more targets than trophies
        && t.checkExpect(moreTarThanTrop.levelWon(), false)

        // test a level with more trophies than targets
        && t.checkExpect(moreTrophThanTar.levelWon(), true)

        // with mismatched colors
        && t.checkExpect(wrongColors.levelWon(), false);
  }

  // LEVEL WON HELPERS

  boolean testWrongColor(Tester t) {
    return t.checkExpect(testTrophyRed.wrongColor("red"), false)
        && t.checkExpect(testTrophyRed.wrongColor("blue"), true)
        && t.checkExpect(testBlankContent.wrongColor("blue"), true);
  }

  boolean testEmptyTarget(Tester t) {
    return t.checkExpect(testTargetRed.emptyTarget(testTrophyRed), false)
        && t.checkExpect(testTargetRed.emptyTarget(testTrophyBlue), true)
        && t.checkExpect(testTargetRed.emptyTarget(testBlankContent), true);
  }

  // ---------- TESTS FOR LEVEL FUNCTIONALITY ---------- //

  boolean testGetPlayerInBoard(Tester t) {
    return t.checkExpect(this.testLevel.getPlayerInBoard(),
        new Cell(3, 3, new BlankGroundContent(), new Player()))

        && t.checkExpect(this.withHole.getPlayerInBoard(),
            new Cell(1, 2, new BlankGroundContent(), new Player()));
  }

  boolean testLevelRender(Tester t) {
    return t.checkExpect(this.twoByTwo.render(), new AboveImage(
        new AboveImage(new EmptyImage(), new BesideImage(
            new BesideImage(new EmptyImage(),
                new OverlayImage(new BlankLevelContent().renderPiece(),
                    new BlankGroundContent().renderPiece())),
            new OverlayImage(new Wall().renderPiece(), new BlankGroundContent().renderPiece()))),
        new BesideImage(
            new BesideImage(new EmptyImage(),
                new OverlayImage(new BlankLevelContent().renderPiece(),
                    new BlankGroundContent().renderPiece())),
            new OverlayImage(new Wall().renderPiece(), new BlankGroundContent().renderPiece()))));
  }

  boolean testShouldEnd(Tester t) {
    // a level with no player should end
    return t.checkExpect(this.noPlayer.shouldEnd(), true)
        // a level with all targets covered by trophies that match in color should end
        && t.checkExpect(this.testLevelWon.shouldEnd(), true)
        // a level with not all targets covered by trophies that match in color should
        // not end
        && t.checkExpect(this.testLevelAlmostWon.shouldEnd(), false)
        // a level with a player and uncovered targets should not end
        && t.checkExpect(this.testLevel.shouldEnd(), false);
  }
  // ---------- TESTS FOR CELL FUNCTIONALITY ---------- //

  boolean testEditCellGiven(Tester t) {

    Cell blank = new Cell(1, 1, new BlankGroundContent(), new BlankLevelContent());
    Cell player = new Cell(1, 1, new BlankGroundContent(), new Player());

    return t.checkExpect(blank.editCellGiven(player), player)
        && t.checkExpect(player.editCellGiven(blank), blank);
  }

  boolean testEditCellBlank(Tester t) {
    Cell blank = new Cell(1, 1, new BlankGroundContent(), new BlankLevelContent());
    Cell box = new Cell(1, 1, new BlankGroundContent(), new Box());
    Cell player = new Cell(1, 1, new BlankGroundContent(), new Player());
    Cell hole = new Cell(1, 1, new BlankGroundContent(), new Hole());

    return t.checkExpect(player.editCellBlank(), blank) && t.checkExpect(box.editCellBlank(), blank)
        && t.checkExpect(hole.editCellBlank(), blank);
  }

  boolean testCanBeLost(Tester t) {
    Cell box = new Cell(1, 1, new BlankGroundContent(), new Box());
    Cell player = new Cell(1, 1, new BlankGroundContent(), new Player());
    Cell hole = new Cell(1, 1, new BlankGroundContent(), new Hole());

    return t.checkExpect(player.cellCanBeLost(), false) && t.checkExpect(box.cellCanBeLost(), false)
        && t.checkExpect(hole.cellCanBeLost(), true);
  }

  boolean testCanHoldPlayer(Tester t) {
    Cell blank = new Cell(1, 1, new BlankGroundContent(), new BlankLevelContent());
    Cell box = new Cell(1, 1, new BlankGroundContent(), new Box());
    Cell target = new Cell(1, 1, new Target("red"), new BlankLevelContent());
    Cell hole = new Cell(1, 1, new BlankGroundContent(), new Hole());

    return t.checkExpect(target.cellCanHoldPlayer(), true)
        && t.checkExpect(box.cellCanHoldPlayer(), false)
        && t.checkExpect(hole.cellCanHoldPlayer(), false)
        && t.checkExpect(blank.cellCanHoldPlayer(), true);
  }

  boolean testCanBePushed(Tester t) {
    Cell trophy = new Cell(1, 1, new BlankGroundContent(), new Trophy("blue"));
    Cell box = new Cell(1, 1, new BlankGroundContent(), new Box());
    Cell target = new Cell(1, 1, new Target("red"), new BlankLevelContent());
    Cell hole = new Cell(1, 1, new BlankGroundContent(), new Hole());

    return t.checkExpect(target.cellCanBePushed(), false)
        && t.checkExpect(box.cellCanBePushed(), true)
        && t.checkExpect(hole.cellCanBePushed(), false)
        && t.checkExpect(trophy.cellCanBePushed(), true);
  }

  // ---------- TESTS FOR SIMPLE MOVEMENT ---------- //

  boolean testMovePlayerRight(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonRight = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb__>ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.newLevelHorizontal(0, 1), testLevelAlmostWonRight);
  }

  boolean testMovePlayerLeft(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonLeft = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb>__ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.newLevelHorizontal(0, -1), testLevelAlmostWonLeft);
  }

  boolean testMovePlayerUp(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonDown = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb___ByW\n" + "WW_>WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonDown.newLevelVertical(-1, 0), testLevelAlmostWonLocal);
  }

  boolean testMovePlayerDown(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonDown = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb___ByW\n" + "WW_>WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.newLevelVertical(1, 0), testLevelAlmostWonDown);
  }

  boolean testMoveAgainstWallRight(Tester t) {

    Level moveAgainstWallRight = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W_____>W\n" + "W__r___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(moveAgainstWallRight.newLevelHorizontal(0, 1), moveAgainstWallRight);
  }

  boolean testMoveAgainstWallLeft(Tester t) {

    Level moveAgainstWallLeft = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W<_____W\n" + "W__r___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(moveAgainstWallLeft.newLevelHorizontal(0, -1), moveAgainstWallLeft);
  }

  boolean testMoveAgainstWallUp(Tester t) {

    Level moveAgainstWallUp = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W<_____W\n" + "W______W\n" + "W__r___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(moveAgainstWallUp.newLevelVertical(-1, 0), moveAgainstWallUp);
  }

  boolean testMoveAgainstWallDown(Tester t) {

    Level moveAgainstWallDown = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__r___W\n" + "W______W\n" + "W___>__W\n"
            + "WWWWWWWW");

    return t.checkExpect(moveAgainstWallDown.newLevelVertical(1, 0), moveAgainstWallDown);
  }

  boolean testMovePushRight(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>r__W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedRight = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W___>r_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>B__W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedRight = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W___>B_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelHorizontal(0, 1), trophyPushedRight)
        && t.checkExpect(boxToPush.newLevelHorizontal(0, 1), boxPushedRight);

  }

  boolean testMovePushLeft(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W_r>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedLeft = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "Wr>____W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W_B>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedLeft = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WB>____W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelHorizontal(0, -1), trophyPushedLeft)
        && t.checkExpect(boxToPush.newLevelHorizontal(0, -1), boxPushedLeft);

  }

  boolean testMovePushUp(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedUp = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedUp = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelVertical(-1, 0), trophyPushedUp)
        && t.checkExpect(boxToPush.newLevelVertical(-1, 0), boxPushedUp);
  }

  boolean testMovePushDown(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedDown = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedDown = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelVertical(1, 0), trophyPushedDown)
        && t.checkExpect(boxToPush.newLevelVertical(1, 0), boxPushedDown);
  }

  boolean testMoveCantPushRight(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>rB_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>BB_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>rW_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>BW_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelHorizontal(0, 1), trophyToPush)
        && t.checkExpect(boxToPush.newLevelHorizontal(0, 1), boxToPush)
        && t.checkExpect(trophyToPush2.newLevelHorizontal(0, 1), trophyToPush2)
        && t.checkExpect(boxToPush2.newLevelHorizontal(0, 1), boxToPush2);
  }

  boolean testMoveCantPushLeft(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WBr>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WBB>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WWr>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WWB>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelHorizontal(0, -1), trophyToPush)
        && t.checkExpect(boxToPush.newLevelHorizontal(0, -1), boxToPush)
        && t.checkExpect(trophyToPush2.newLevelHorizontal(0, -1), trophyToPush2)
        && t.checkExpect(boxToPush2.newLevelHorizontal(0, -1), boxToPush2);

  }

  boolean testMoveCantPushUp(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__B___W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__B___W\n" + "W__B___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__W___W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__W___W\n" + "W__B___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelVertical(-1, 0), trophyToPush)
        && t.checkExpect(boxToPush.newLevelVertical(-1, 0), boxToPush)
        && t.checkExpect(trophyToPush2.newLevelVertical(-1, 0), trophyToPush2)
        && t.checkExpect(boxToPush2.newLevelVertical(-1, 0), boxToPush2);
  }

  boolean testMoveCantPushDown(Tester t) {

    Level trophyToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__B___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__B___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__W___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPush2 = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__W___W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPush.newLevelVertical(1, 0), trophyToPush)
        && t.checkExpect(boxToPush.newLevelVertical(1, 0), boxToPush)
        && t.checkExpect(trophyToPush2.newLevelVertical(1, 0), trophyToPush2)
        && t.checkExpect(boxToPush2.newLevelVertical(1, 0), boxToPush2);
  }

  boolean testMoveIntoHoleRight(Tester t) {

    Level holesAroundPlayer = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh>hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    Level movedIntoHoleRight = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh__r_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    return t.checkExpect(holesAroundPlayer.newLevelHorizontal(0, 1), movedIntoHoleRight);
  }

  boolean testMoveIntoHoleLeft(Tester t) {

    Level holesAroundPlayer = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh>hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    Level movedIntoHoleLeft = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "W__hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    return t.checkExpect(holesAroundPlayer.newLevelHorizontal(0, -1), movedIntoHoleLeft);
  }

  boolean testMoveIntoHoleUp(Tester t) {

    Level holesAroundPlayer = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh>hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    Level movedIntoHoleUp = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_____W\n" + "Wh_hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    return t.checkExpect(holesAroundPlayer.newLevelVertical(-1, 0), movedIntoHoleUp);
  }

  boolean testMoveIntoHoleDown(Tester t) {

    Level holesAroundPlayer = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh>hr_W\n" + "W_h___W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    Level movedIntoHoleDown = new Level(
        "_______\n" + "____R__\n" + "_______\n" + "_______\n" + "_______\n" + "_______\n"
            + "_______\n" + "_______",
        "WWWWWWW\n" + "W_____W\n" + "W_h___W\n" + "Wh_hr_W\n" + "W_____W\n" + "W_____W\n"
            + "W_____W\n" + "WWWWWWW");

    return t.checkExpect(holesAroundPlayer.newLevelVertical(1, 0), movedIntoHoleDown);
  }

  boolean testPushIntoHoleRight(Tester t) {

    Level trophyToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>rh_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedRightIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W___>__W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>Bh_W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedRightIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W___>__W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPushIntoHole.newLevelHorizontal(0, 1), trophyPushedRightIntoHole)
        && t.checkExpect(boxToPushIntoHole.newLevelHorizontal(0, 1), boxPushedRightIntoHole);

  }

  boolean testPushIntoHoleLeft(Tester t) {

    Level trophyToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "Whr>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedLeftIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W_>____W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "WhB>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedLeftIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W_>____W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPushIntoHole.newLevelHorizontal(0, -1), trophyPushedLeftIntoHole)
        && t.checkExpect(boxToPushIntoHole.newLevelHorizontal(0, -1), boxPushedLeftIntoHole);

  }

  boolean testPushIntoHoleUp(Tester t) {

    Level trophyToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__h___W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedUpIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W______W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W__h___W\n" + "W__r___W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedUpIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W______W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPushIntoHole.newLevelVertical(-1, 0), trophyPushedUpIntoHole)
        && t.checkExpect(boxToPushIntoHole.newLevelVertical(-1, 0), boxPushedUpIntoHole);
  }

  boolean testPushIntoHoleDown(Tester t) {

    Level trophyToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__h___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level trophyPushedDownIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxToPushIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W__>___W\n" + "W__r___W\n" + "W__h___W\n" + "W______W\n"
            + "WWWWWWWW");

    Level boxPushedDownIntoHole = new Level(
        "________\n" + "__R_____\n" + "________\n" + "________\n" + "________\n" + "________\n"
            + "________",
        "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W__>___W\n" + "W______W\n" + "W______W\n"
            + "WWWWWWWW");

    return t.checkExpect(trophyToPushIntoHole.newLevelVertical(1, 0), trophyPushedDownIntoHole)
        && t.checkExpect(boxToPushIntoHole.newLevelVertical(1, 0), boxPushedDownIntoHole);
  }

  // tests for editBoard
  boolean testEditPlayerMoveLeft(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonLeft = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb>__ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.editBoardHorizontal(3, 3, 0, -1),
        testLevelAlmostWonLeft);
  }

  boolean testEditPlayerMoveUp(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonUp = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__WrWW__\n" + "WWW>_WWW\n" + "Wb___ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.editBoardVertical(3, 3, -1, 0),
        testLevelAlmostWonUp);
  }

  boolean testEditPlayerIntoHole(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wbh>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonInHole = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb___ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    return t.checkExpect(testLevelAlmostWonLocal.editBoardHorizontal(3, 3, 0, -1),
        testLevelAlmostWonInHole);
  }

  // ---------- TESTS FOR ICE HELPERS ---------- //
  // there are 3 ice blocks to the right of the player
  boolean testCountIceHorizontal(Tester t) {
    return t.checkExpect(withIceTrophy.countIceHorizontal(withIceBig.board.get(4), 1), 3);
  }

  // there is no ice to the left of the player
  boolean testCountNoIceHorizontal(Tester t) {
    return t.checkExpect(withSlipNSlide.countIceHorizontal(withSlipNSlide.board.get(4), -1), 0);
  }

  // there is no ice beneath the player
  boolean testCountNoIceVertical(Tester t) {
    return t.checkExpect(withVerticalIce.countIceVertical(withIceBig.board, 1), 0);
  }

  // there are 3 ice blocks above the player, but the player can only move up 2
  // blocks,
  // so the method returns 2
  boolean testCountIceVertical(Tester t) {
    return t.checkExpect(withVerticalIce.countIceVertical(withVerticalIce.board, -1), 2);
  }

  // there is ice to the right of the player with a movable object on top of the
  // ice
  boolean testIceWithMovableHorizontal(Tester t) {
    return t.checkExpect(withIceTrophy2.iceWithMovableHorizontal(withIceTrophy2.board.get(4), 1),
        true);
  }

  // there is NO ice to the left of the player with a movable object on top of the
  // ice
  boolean testNoIceWithMovableHorizontal(Tester t) {
    return t.checkExpect(withVerticalIce.iceWithMovableHorizontal(withIceTrophy2.board.get(4), -1),
        false);
  }

  // there is ice to the right of the player with NO movable objects on top of the
  // ice
  boolean testIceWithNoMovableHorizontal(Tester t) {
    return t.checkExpect(withIce.iceWithMovableHorizontal(withIce.board.get(2), 1), false);
  }

  // there is ice above the player with a movable object on top of the ice
  boolean testIceWithMovableVertical(Tester t) {
    return t.checkExpect(withVerticalIce.iceWithMovableVertical(withVerticalIce.board, -1), true);
  }

  // there is NO ice below the player with a movable object on top of the ice
  boolean testNoIceWithMovableVertical(Tester t) {
    return t.checkExpect(withVerticalIce.iceWithMovableVertical(withVerticalIce.board, 1), false);
  }

  //there is ice below the player with NO movable object on top of the ice
  boolean testIceWithNoMovableVertical(Tester t) {
    return t.checkExpect(withVerticalIce.iceWithMovableVertical(withVerticalIce.board, 1), false);
  }

  // ---------- TESTS FOR SCORE-KEEPING ---------- //
  boolean testScoreAfter1Move(Tester t) {
    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonRight = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb__>ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    SokobanWorld testWorldAfterMoveExpected = new SokobanWorld(testLevelAlmostWonRight, 
        new SokobanWorld(testLevelAlmostWonLocal), new SokobanWorld(testLevelAlmostWonLocal), 1);
    
    SokobanWorld testWorld = new SokobanWorld(testLevelAlmostWonLocal);

   return t.checkExpect(testWorld.onKeyEvent("right").score, testWorldAfterMoveExpected.score);
    
  }

  boolean testScoreAfterUndo(Tester t) {
    
    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonRight = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb__>ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    SokobanWorld testWorldUndone = new SokobanWorld(testLevelAlmostWonLocal);

    SokobanWorld testWorld = new SokobanWorld(testLevelAlmostWonRight, testWorldUndone,
        testWorldUndone, 1);

    return t.checkExpect(testWorld.onKeyEvent("u").score, testWorldUndone.score);

  }

  // ---------- TESTS FOR UNDO ---------- //
  boolean testMovePlayerRightAndUndo(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonRight = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb__>ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    SokobanWorld testWorldUndone = new SokobanWorld(testLevelAlmostWonLocal);

    SokobanWorld testWorld = new SokobanWorld(testLevelAlmostWonRight, testWorldUndone,
        testWorldUndone, 1);

    return t.checkExpect(testWorld.onKeyEvent("u"), testWorldUndone);
  }

  boolean testMovePlayerDownAndUndo(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelAlmostWonDown = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "Wb___ByW\n" + "WW_>WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    SokobanWorld testWorldUndone = new SokobanWorld(testLevelAlmostWonLocal);

    SokobanWorld testWorld = new SokobanWorld(testLevelAlmostWonDown, testWorldUndone,
        testWorldUndone, 1);

    return t.checkExpect(testWorld.onKeyEvent("u"), testWorldUndone);
  }

  boolean testTrophyInHoleAndUndo(Tester t) {

    Level testLevelAlmostWonLocal = new Level(
        "________\n" + "________\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__WhWW__\n" + "WWWr_WWW\n" + "Wb_>_ByW\n" + "WW__WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    Level testLevelWithTrophyInHole = new Level(
        "________\n" + "___R____\n" + "________\n" + "_B____Y_\n" + "________\n" + "___G____\n"
            + "________",
        "__WWW___\n" + "__W_WW__\n" + "WWW>_WWW\n" + "Wb___ByW\n" + "WW_>WWWW\n" + "_WWgW___\n"
            + "__WWW___");

    SokobanWorld testWorldUndone = new SokobanWorld(testLevelAlmostWonLocal);

    SokobanWorld testWorld = new SokobanWorld(testLevelWithTrophyInHole, testWorldUndone,
        testWorldUndone, 1);

    return t.checkExpect(testWorld.onKeyEvent("u"), testWorldUndone);
  }
  
//TESTS FOR MOVING ON ICE 

 boolean testEditPlayerWithIceRight(Tester t) {

   Level withIce = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceRight = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_____>W\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withIce.newLevelHorizontal(0, 1), withIceRight);
 }

 boolean testEditPlayerWithIceLeft(Tester t) {

   Level withIce = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_____>W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceLeft = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withIce.newLevelHorizontal(0, -1), withIceLeft);
 }

 boolean testEditPlayerWithIceUp(Tester t) {

   Level withIce = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");
   
   Level withIceUp = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIce.newLevelVertical(-1, 0), withIceUp);
 }

 boolean testEditPlayerWithIceDown(Tester t) {

   Level withIce = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");
   
   Level withIceDown = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIce.newLevelVertical(1, 0), withIceDown);
 }
 
 boolean testEditPlayerWithIceAndTrophyRight(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_y___W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophyRight = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W____>yW\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withIceAndTrophy.newLevelHorizontal(0, 1), withIceAndTrophyRight);
 }

 boolean testEditPlayerWithIceAndTrophyLeft(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W__y_<_W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophyLeft = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "Wy<____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withIceAndTrophy.newLevelHorizontal(0, -1), withIceAndTrophyLeft);
 }

 boolean testEditPlayerWithIceAndTrophyUp(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W_y____W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");
   
   Level withIceUpAndTrophyUp = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_y____W\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(-1, 0), withIceUpAndTrophyUp);
 }

 boolean testEditPlayerWithIceAndTrophyDown(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W_y____W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");
   
   Level withIceAndTrophyDown = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n" + "W_y____W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(1, 0), withIceAndTrophyDown);
 }
 
 boolean testWithIceAndTrophyBeforeIceRight(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__Y_____\n" + "___II___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>y____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophyRight = new Level("________\n" + "__Y_____\n" + "___II___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_>__y_W\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withIceAndTrophy.newLevelHorizontal(0, 1), withIceAndTrophyRight);
 }

 boolean testWithIceAndTrophyBeforeIceLeft(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W____y<W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophyLeft = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "Wy___<_W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withIceAndTrophy.newLevelHorizontal(0, -1), withIceAndTrophyLeft);
 }

 boolean testWithIceAndTrophyBeforeIceUp(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_y____W\n" + "W_>____W\n"
           + "WWWWWWWW");
   
   Level withIceUpAndTrophyUp = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_y____W\n" + "W______W\n" + "W______W\n" + "W_>____W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(-1, 0), withIceUpAndTrophyUp);
 }

 boolean testWithIceAndTrophyBeforeIceDown(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W_y____W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");
   
   Level withIceAndTrophyDown = new Level("________\n" + "________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W_y____W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(1, 0), withIceAndTrophyDown);
 }
 
 boolean testWithIceAndTwoTrophiesRight(Tester t) {

   Level withIceAndTrophies = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_yy__W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophiesRight = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_>yy__W\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withIceAndTrophies.newLevelHorizontal(0, 1), withIceAndTrophiesRight);
 }

 boolean testWithIceAndTwoTrophiesLeft(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_yy_<_W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceAndTrophyLeft = new Level("________\n" + "__Y_____\n" + "__III___\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_yy<__W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withIceAndTrophy.newLevelHorizontal(0, -1), withIceAndTrophyLeft);
 }

 boolean testWithIceAndTwoTrophiesUp(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W_y____W\n" + "W_y____W\n" + "W______W\n" + "W_<____W\n"
           + "WWWWWWWW");
   
   Level withIceUpAndTrophyUp = new Level("________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W_y____W\n" + "W_y____W\n" + "W_<____W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(-1, 0), withIceUpAndTrophyUp);
 }

 boolean testWithIceAndTwoTrophiesDown(Tester t) {

   Level withIceAndTrophy = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W_y____W\n" + "W_y____W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");
   
   Level withIceAndTrophyDown = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W_y____W\n" + "W_y____W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceAndTrophy.newLevelVertical(1, 0), withIceAndTrophyDown);
 }

 boolean testEditPlayerWithIceToWallRight(Tester t) {

   Level withIceToWall = new Level("________\n" + "__Y_____\n" + "__IIIII_\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceToWallRight = new Level("________\n" + "__Y_____\n" + "__IIIII_\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_____>W\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withIceToWall.newLevelHorizontal(0, 1), withIceToWallRight);
 }

 boolean testEditPlayerWithIceToWallLeft(Tester t) {

   Level withIceToWall = new Level("________\n" + "__Y_____\n" + "_IIIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W_____>W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withIceToWallLeft = new Level("________\n" + "__Y_____\n" + "_IIIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>_____W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withIceToWall.newLevelHorizontal(0, -1), withIceToWallLeft);
 }

 boolean testEditPlayerWithIceToWallUp(Tester t) {

   Level withIceToWall = new Level("________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");
   
   Level withIceToWallUp = new Level("________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceToWall.newLevelVertical(-1, 0), withIceToWallUp);
 }

 boolean testEditPlayerWithIceToWallDown(Tester t) {

   Level withIceToWall = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");
   
   Level withIceToWallDown = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "__I_____\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");

   return t.checkExpect(withIceToWall.newLevelVertical(1, 0), withIceToWallDown);
 }
 
 boolean testSlipNSlideRight(Tester t) {

   Level withSlipNSlide = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W>____hW\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withSlipNSlideRight = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W______W\n" + "WW___WWW\n" + "_WWWWW__");

   return t.checkExpect(withSlipNSlide.newLevelHorizontal(0, 1), withSlipNSlideRight);
 }

 boolean testSlipNSlideLeft(Tester t) {

   Level withSlipNSlide = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "Wh____>W\n" + "WW___WWW\n" + "_WWWWW__");
   
   Level withSlipNSlideLeft = new Level("________\n" + "__Y_____\n" + "__IIII__\n" + "________\n" + "________",
       "_WWWWWWW\n" + "W______W\n" + "W______W\n" + "WW___WWW\n" + "_WWWWW__");
   
   return t.checkExpect(withSlipNSlide.newLevelHorizontal(0, -1), withSlipNSlideLeft);
 }

 boolean testSlipNSlideUp(Tester t) {

   Level withSlipNSlide = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_h____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_>____W\n"
           + "WWWWWWWW");
   
   Level withSlipNSlideUp = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withSlipNSlide.newLevelVertical(-1, 0), withSlipNSlideUp);
 }

 boolean testSlipNSlideDown(Tester t) {

   Level withSlipNSlide = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W_>____W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W_h____W\n"
           + "WWWWWWWW");
   
   Level withSlipNSlideDown = new Level("________\n" + "________\n" + "__I_____\n" + "__I_____\n" + "__I_____\n" + "________\n"
       + "________",
       "WWWWWWWW\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n" + "W______W\n"
           + "WWWWWWWW");

   return t.checkExpect(withSlipNSlide.newLevelVertical(1, 0), withSlipNSlideDown);
 }
 

  boolean testBigBang(Tester t) {

    SokobanWorld w = new SokobanWorld(this.testLevelAlmostWon);
    int worldWidth = 500;
    int worldHeight = 500;
    double tickRate = 3;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }

}