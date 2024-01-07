import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.impworld.*; // the abstract World class and the big-bang library for imperative worlds
import java.awt.Color; // general colors (as triples of red,green,blue values)
                       // and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.util.ArrayList;

// represents a cell in cellular automata
interface ICell {
  // gets the state of this ICell
  int getState();

  // render this ICell as an image of a rectangle with this width and height
  WorldImage render(int width, int height);

  // produces the child cell of this ICell with the given left and right neighbors
  ICell childCell(ICell left, ICell right);
}

//represents a cell that is always off and produces another inert cell as its child 
//regardless of its neighbors' states 
class InertCell implements ICell {
  // this inert cell always has a state of 0 ("off")
  public int getState() {
    return 0;
  }

  // renders this cell as a white rectangle, since this inert cell is always off
  public WorldImage render(int width, int height) {
    return new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE);
  }

  // this inert cell produces another inert cell as its child and ignores its
  // neighbors entirely
  public ICell childCell(ICell left, ICell right) {
    return new InertCell();
  }
}

//abstracted class for rules: this rule-based cell is constructed with its own state and a ruleset consisting of 
//ICells that correspond to this rule's output.
abstract class ARule implements ICell {
  int state;

  ARule(int state) {
    this.state = state;
  }

  // returns this rule-based cell's state
  public int getState() {
    return this.state;
  }

  // renders this rule-based cell as a rectangle with a color depending on its
  // state
  public WorldImage render(int width, int height) {
    if (state == 0) {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE);
    }
    else if (state == 1) {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.BLACK);
    }
    else {
      throw new IllegalStateException("cell can only have state of either 0 or 1");
    }
  }

  // childCell is not yet abstracted: One idea to abstract the method is for ARule
  // to take in an ArrayList<Integer> of size 8 with
  // the 8 binary outcomes of a rule called 'ruleOutputs'. Then for each condition
  // (left = some num, this = some num, and right = some num), a local var 'index'
  // is set to be the that case (ex: if left = 1, this = 1, right = 1, then index
  // = 0 since this is the first case). Then, at the end,
  // the method returns a new Rule(ruleOutputs.get(index). The issue arises with
  // returning the child cell of the same type as its parent cell,
  // since the ARule class cannot return an instance of a specific rule (ex: child
  // cell in ARule cannot return new Rule60(ruleOutputs.get(index))
  // since every rule cannot produce a Rule60 child cell.

}

// represents a rule-based cell who's child cell is determined based on the output 
// of this rule's cell and its neighbors
// Rule60's outputs (in binary) are 0, 0, 1, 1, 1, 1, 0, 0
class Rule60 extends ARule {

  Rule60(int state) {
    super(state);
  }

  // returns the child cell of this rule60 cell based on its own state and the
  // states of its neighbors
  public ICell childCell(ICell left, ICell right) {
    int leftState = left.getState();
    int rightState = right.getState();
    int childState = 0;
    if (leftState == 1 && this.state == 1 && rightState == 1) {
      childState = 0;
    }
    else if (leftState == 1 && this.state == 1 && rightState == 0) {
      childState = 0;
    }
    else if (leftState == 1 && this.state == 0 && rightState == 1) {
      childState = 1;
    }
    else if (leftState == 1 && this.state == 0 && rightState == 0) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 1 && rightState == 1) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 1 && rightState == 0) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 0 && rightState == 1) {
      childState = 0;
    }
    else if (leftState == 0 && this.state == 0 && rightState == 0) {
      childState = 0;
    }
    return new Rule60(childState);
  }
}

// represents a rule-based cell who's child cell is determined based on the output 
// of this rule's cell and its neighbors
// Rule30's outputs (in binary) are 0, 0, 0, 1, 1, 1, 1, 0
class Rule30 extends ARule {

  Rule30(int state) {
    super(state);
  }

  // returns the child cell of this rule30 cell based on its own state and the
  // states of its neighbors
  public ICell childCell(ICell left, ICell right) {
    int leftState = left.getState();
    int rightState = right.getState();
    int childState = 0;
    if (leftState == 1 && this.state == 1 && rightState == 1) {
      childState = 0;
    }
    else if (leftState == 1 && this.state == 1 && rightState == 0) {
      childState = 0;
    }
    else if (leftState == 1 && this.state == 0 && rightState == 1) {
      childState = 0;
    }
    else if (leftState == 1 && this.state == 0 && rightState == 0) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 1 && rightState == 1) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 1 && rightState == 0) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 0 && rightState == 1) {
      childState = 1;
    }
    else if (leftState == 0 && this.state == 0 && rightState == 0) {
      childState = 0;
    }
    return new Rule30(childState);
  }
}

//represents a population of cells
class CellArray {
  ArrayList<ICell> cells = new ArrayList<ICell>();

  CellArray(ArrayList<ICell> c) {
    this.cells = c;
  }

// produces the next generation of cells from this CellArray's population
  public CellArray nextGen() {
    ArrayList<ICell> next = new ArrayList<ICell>();
    // iterates through this CellArray's population, one by one. If the cell's index
    // is 0, then we calculate that cell's child with an inertCell as its left
    // neighbor. Similarly, if the cell's index is the population AL's size - 1
    // (meaning we are at the end of this list), then we calculate that cell's child
    // with an inertCell as its right neighbor.
    for (int i = 0; i < cells.size(); i = i + 1) {
      ICell main = cells.get(i);
      if (i == 0) {
        next.add(main.childCell(new InertCell(), cells.get(i + 1)));
      }
      else if (i == cells.size() - 1) {
        next.add(main.childCell(cells.get(i - 1), new InertCell()));
      }
      else {
        next.add(main.childCell(cells.get(i - 1), cells.get(i + 1)));
      }
    }
    return new CellArray(next);
  }

  // renders this CellArray's population as an image
  public WorldImage draw(int cellWidth, int cellHeight) {
    WorldImage base = cells.get(0).render(cellWidth, cellHeight);
    // this loop is meant to implement a foldl-type function: the list iterates
    // through all of the cells in this cellArray and folds it to the
    // be placed beside the base image. Then, the base is modified to included the
    // current image, and the process is repeated until the base
    // world image consists of this cellArray's population rendered as one image.
    for (int i = 1; i < cells.size(); i++) {
      WorldImage current = cells.get(i).render(cellWidth, cellHeight);
      base = new BesideImage(base, current);
    }
    return base;
  }
}

// represents a cellular automata world
class CAWorld extends World {

  // constants
  static final int CELL_WIDTH = 10;
  static final int CELL_HEIGHT = 10;
  static final int INITIAL_OFF_CELLS = 20;
  static final int TOTAL_CELLS = INITIAL_OFF_CELLS * 2 + 1;
  static final int NUM_HISTORY = 41;
  static final int TOTAL_WIDTH = TOTAL_CELLS * CELL_WIDTH;
  static final int TOTAL_HEIGHT = NUM_HISTORY * CELL_HEIGHT;

  // the current generation of cells
  CellArray curGen;
  // the history of previous generations (earliest state at the start of the list)
  ArrayList<CellArray> history;

  // Constructs a CAWorld with INITIAL_OFF_CELLS of off cells on the left,
  // then one on cell, then INITIAL_OFF_CELLS of off cells on the right
  CAWorld(ICell off, ICell on) {
    // TODO: Fill in
    ArrayList<ICell> population = new ArrayList<ICell>();

    for (int i = 0; i < INITIAL_OFF_CELLS; i += 1) {
      population.add(off);
    }

    population.add(on);

    for (int i = 0; i < INITIAL_OFF_CELLS; i += 1) {
      population.add(off);
    }

    this.curGen = new CellArray(population);
    this.history = new ArrayList<CellArray>();
  }

  // Modifies this CAWorld by adding the current generation to the history
  // and setting the current generation to the next one
  public void onTick() {
    // TODO: Fill in
    this.history.add(this.curGen);
    this.curGen = this.curGen.nextGen();
  }

  // Draws the current world, ``scrolling up'' from the bottom of the image
  public WorldImage makeImage() {
    // make a light-gray background image big enough to hold 41 generations of 41
    // cells each
    WorldImage bg = new RectangleImage(TOTAL_WIDTH, TOTAL_HEIGHT, OutlineMode.SOLID,
        new Color(240, 240, 240));

    // build up the image containing the past and current cells
    WorldImage cells = new EmptyImage();
    for (CellArray array : this.history) {
      cells = new AboveImage(cells, array.draw(CELL_WIDTH, CELL_HEIGHT));
    }
    cells = new AboveImage(cells, this.curGen.draw(CELL_WIDTH, CELL_HEIGHT));

    // draw all the cells onto the background
    return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM, cells, 0, 0, bg);
  }

  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(TOTAL_WIDTH, TOTAL_HEIGHT);
    canvas.placeImageXY(this.makeImage(), TOTAL_WIDTH / 2, TOTAL_HEIGHT / 2);
    return canvas;
  }
}

class ExamplesAutomata {
  // tests inert cell rendering as a white rectangle
  boolean testInertRender(Tester t) {
    return t.checkExpect(new InertCell().render(10, 10),
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.WHITE));
  }

  // tests inert cell child is inert regardless of input
  boolean testInertChild(Tester t) {
    return t.checkExpect(new InertCell().childCell(new Rule60(1), new Rule60(0)), new InertCell());
  }

  // tests rendering an on rule60 cell
  boolean testRule60Render(Tester t) {
    return t.checkExpect(new Rule60(1).render(10, 10),
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLACK));
  }

  // tests that the rule60 cell's child cell is correct according to case 2 (left
  // = 1, this = 1, right = 0 => child = 0)
  boolean testRule60ChildCase2(Tester t) {
    return t.checkExpect(new Rule60(1).childCell(new Rule60(1), new Rule60(0)), new Rule60(0));
  }

  // tests that the rule60 cell's child cell is correct according to case 5 (left
  // = 0, this = 1, right = 1 => child = 1)
  boolean testRule60ChildCase5(Tester t) {
    return t.checkExpect(new Rule60(1).childCell(new Rule60(0), new Rule60(1)), new Rule60(1));
  }

  //tests rendering an off rule30 cell
  boolean testRule30Render(Tester t) {
    return t.checkExpect(new Rule30(0).render(10, 10),
        new RectangleImage(10, 10, OutlineMode.SOLID, Color.WHITE));
  }

  // tests that the rule30 cell's child cell is correct according to case 3 (left
  // = 1, this = 0, right = 1 => child = 0)
  boolean testRule30ChildCase3(Tester t) {
    return t.checkExpect(new Rule30(0).childCell(new Rule30(1), new Rule30(1)), new Rule30(0));
  }

  // tests that the rule30 cell's child cell is correct according to case 7 (left
  // = 0, this = 0, right = 1 => child = 1)
  boolean testRule30ChildCase7(Tester t) {
    return t.checkExpect(new Rule30(0).childCell(new Rule30(0), new Rule30(1)), new Rule30(1));
  }

  // testing next gen on the first gen of rule60
  boolean testRule60NextGen(Tester t) {
    ArrayList<ICell> gen1 = new ArrayList<ICell>();
    ArrayList<ICell> gen2 = new ArrayList<ICell>();
    for (int i = 0; i < 41; i++) {
      if (i == 20) {
        gen1.add(new Rule60(1));
      }
      else {
        gen1.add(new Rule60(0));
      }
    }
    for (int i = 0; i < 41; i++) {
      if (i == 20 || i == 21) {
        gen2.add(new Rule60(1));
      }
      else {
        gen2.add(new Rule60(0));
      }
    }
    CellArray rule60gen1 = new CellArray(gen1);
    CellArray rule60gen2 = new CellArray(gen2);

    return t.checkExpect(rule60gen1.nextGen(), rule60gen2);
  }

  // testing next gen on the first gen of rule30
  boolean testRule30NextGen(Tester t) {
    ArrayList<ICell> gen1 = new ArrayList<ICell>();
    ArrayList<ICell> gen2 = new ArrayList<ICell>();
    for (int i = 0; i < 41; i++) {
      if (i == 20) {
        gen1.add(new Rule30(1));
      }
      else {
        gen1.add(new Rule30(0));
      }
    }
    for (int i = 0; i < 41; i++) {
      if (i == 19 || i == 20 || i == 21) {
        gen2.add(new Rule30(1));
      }
      else {
        gen2.add(new Rule30(0));
      }
    }
    CellArray rule30gen1 = new CellArray(gen1);
    CellArray rule30gen2 = new CellArray(gen2);

    return t.checkExpect(rule30gen1.nextGen(), rule30gen2);
  }

  void testBigBang(Tester t) {
    CAWorld w = new CAWorld(new Rule60(0), new Rule60(1));
    int worldWidth = 500;
    int worldHeight = 500;
    double tickRate = 100000;
    w.bigBang(worldWidth, worldHeight, tickRate);
  }

  // tested image rendering using funworld // test inert cell/off cell rendering
  /*
   * boolean testInertCellRender(Tester t) { WorldCanvas c = new WorldCanvas(500,
   * 500); WorldScene s = new WorldScene(500, 500); return
   * c.drawScene(s.placeImageXY(new InertCell().render(10, 10), 250, 250)) &&
   * c.show(); }
   * 
   * // test on cell rendering boolean testOnCellRender(Tester t) { WorldCanvas c
   * = new WorldCanvas(500, 500); WorldScene s = new WorldScene(500, 500); return
   * c.drawScene(s.placeImageXY(new Rule60(1).render(10, 10), 250, 250)) &&
   * c.show(); }
   * 
   * boolean testCellArrayRule60Render(Tester t) { WorldCanvas c = new
   * WorldCanvas(500, 500); WorldScene s = new WorldScene(500, 500);
   * ArrayList<ICell> cells = new ArrayList<ICell>(); cells.add(new Rule60(0));
   * cells.add(new Rule60(0)); cells.add(new Rule60(1)); cells.add(new Rule60(1));
   * cells.add(new Rule60(1)); cells.add(new Rule60(1)); cells.add(new Rule60(0));
   * cells.add(new Rule60(0)); CellArray rule60Test = new CellArray(cells); return
   * c.drawScene(s.placeImageXY(rule60Test.draw(10, 10), 250, 250)) && c.show(); }
   */
}
