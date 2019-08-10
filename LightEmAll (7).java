import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// constant class
class Constants {
  static final int PIECE_SIZE = 100;
  static final int PIECE_HALF = PIECE_SIZE / 2;
  static final double LIGHT_RADIUS = PIECE_SIZE / 5;
  static final double TICK_RATE = 0.001;
  static final Color TILE_COLOR = Color.darkGray;
  static final Color LINE_COLOR = Color.gray;
  static final Color EDGE_COLOR = Color.black;
  static final Color LIGHT_COLOR = Color.cyan;
  static final Color POWER_COLOR = Color.yellow;
  static final int LINE_WIDTH = (int) Math.round(PIECE_SIZE / 7);
  static final int LINE_HEIGHT = (int) Math.round(PIECE_SIZE / 2)
      + (int) Math.round(LINE_WIDTH / 2);
}

// represents a game piece
class GamePiece {
  // in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;
  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;
  // whether the power station is on this piece
  boolean powerStation;
  ArrayList<GamePiece> neighbors;
  ArrayList<GamePiece> connected;
  boolean power;

  // default constructor
  GamePiece(int row, int col) {
    this.row = row;
    this.col = col;
    this.left = false;
    this.right = false;
    this.top = false;
    this.bottom = false;
    this.powerStation = false;
    this.neighbors = new ArrayList<GamePiece>();
    this.connected = new ArrayList<GamePiece>();
  }

  // testing constructor
  GamePiece(int row, int col, boolean left, boolean right, boolean top, boolean bottom) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = false;
    this.neighbors = new ArrayList<GamePiece>();
    this.connected = new ArrayList<GamePiece>();
  }

  // set the pieces
  void setPiece(boolean left, boolean right, boolean top, boolean bottom) {
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
  }

  // rotate the piece clockwise once
  void rotate() {
    boolean lef = this.left;
    boolean righ = this.right;
    boolean to = this.top;
    boolean botto = this.bottom;

    this.left = botto;
    this.right = to;
    this.top = lef;
    this.bottom = righ;
  }

  // rotate the piece counter clockwise once
  void counterRotate() {
    boolean lef = this.left;
    boolean righ = this.right;
    boolean to = this.top;
    boolean botto = this.bottom;

    this.left = to;
    this.right = botto;
    this.top = righ;
    this.bottom = lef;
  }

  // Draws this tile onto the background at the specified logical coordinates
  WorldScene drawAt(WorldScene background) {
    int xCoord = ((this.col + 1) * Constants.PIECE_HALF) + (this.col * Constants.PIECE_HALF);
    int yCoord = ((this.row + 1) * Constants.PIECE_HALF) + (this.row * Constants.PIECE_HALF);
    int lineWidth = (int) Math.round(Constants.LINE_WIDTH / 2);
    int lineHeight = (int) Math.round(Constants.LINE_HEIGHT / 2);

    for (int i = 0; i < 5; i++) {
      if (i == 0) {
        if (this.left && this.power) {
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "solid", Constants.TILE_COLOR), xCoord, yCoord);
          background.placeImageXY(new RectangleImage(Constants.LINE_HEIGHT, Constants.LINE_WIDTH,
              "solid", Constants.POWER_COLOR), xCoord + lineWidth - lineHeight, yCoord);
        }
        else if (this.left && !this.power) {
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "solid", Constants.TILE_COLOR), xCoord, yCoord);
          background.placeImageXY(new RectangleImage(Constants.LINE_HEIGHT, Constants.LINE_WIDTH,
              "solid", Constants.LINE_COLOR), xCoord + lineWidth - lineHeight, yCoord);
        }
        else {
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "solid", Constants.TILE_COLOR), xCoord, yCoord);
        }
      }
      else if (i == 1) {
        if (this.right && this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_HEIGHT, Constants.LINE_WIDTH,
              "solid", Constants.POWER_COLOR), xCoord - lineWidth + lineHeight, yCoord);
        }
        else if (this.right && !this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_HEIGHT, Constants.LINE_WIDTH,
              "solid", Constants.LINE_COLOR), xCoord - lineWidth + lineHeight, yCoord);
        }
      }
      else if (i == 2) {
        if (this.top && this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_WIDTH, Constants.LINE_HEIGHT,
              "solid", Constants.POWER_COLOR), xCoord, yCoord + lineWidth - lineHeight);
        }
        else if (this.top && !this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_WIDTH, Constants.LINE_HEIGHT,
              "solid", Constants.LINE_COLOR), xCoord, yCoord + lineWidth - lineHeight);
        }
      }
      else if (i == 3) {
        if (this.bottom && this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_WIDTH, Constants.LINE_HEIGHT,
              "solid", Constants.POWER_COLOR), xCoord, yCoord - lineWidth + lineHeight);
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "outline", Constants.EDGE_COLOR), xCoord, yCoord);
        }
        else if (this.bottom && !this.power) {
          background.placeImageXY(new RectangleImage(Constants.LINE_WIDTH, Constants.LINE_HEIGHT,
              "solid", Constants.LINE_COLOR), xCoord, yCoord - lineWidth + lineHeight);
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "outline", Constants.EDGE_COLOR), xCoord, yCoord);
        }
        else {
          background.placeImageXY(new RectangleImage(Constants.PIECE_SIZE, Constants.PIECE_SIZE,
              "outline", Constants.EDGE_COLOR), xCoord, yCoord);
        }
      }
      else if (i == 4) {
        if (this.powerStation) {
          background.placeImageXY(
              new StarImage(Constants.LIGHT_RADIUS, OutlineMode.SOLID, Constants.LIGHT_COLOR),
              xCoord, yCoord);
        }
      }
    }
    return background;
  }

  // adds respective neighbor board for corner, edge, and middle cases to their
  // own board lists
  void addNeighbors(ArrayList<ArrayList<GamePiece>> board, int xBound, int yBound) {
    if (this.row == 0 && this.col == 0) {
      this.neighbors.add(board.get(this.row).get(this.col + 1));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
    }

    else if (this.row == yBound - 1 && this.col == 0) {
      this.neighbors.add(board.get(this.row - 1).get(this.col));
      this.neighbors.add(board.get(this.row).get(this.col + 1));
    }

    else if (this.row == 0 && this.col == xBound - 1) {
      this.neighbors.add(board.get(this.row).get(this.col - 1));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
    }

    else if (this.row == yBound - 1 && this.col == xBound - 1) {
      this.neighbors.add(board.get(this.row - 1).get(this.col));
      this.neighbors.add(board.get(this.row).get(this.col - 1));
    }

    else if (this.row == 0 && this.col != 0 && this.col != xBound - 1) {
      this.neighbors.add(board.get(this.row).get(this.col - 1));
      this.neighbors.add(board.get(this.row).get(this.col + 1));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
    }

    else if (this.row != 0 && this.row != yBound - 1 && this.col == 0) {
      this.neighbors.add(board.get(this.row - 1).get(this.col));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
      this.neighbors.add(board.get(this.row).get(this.col + 1));
    }

    else if (this.row == yBound - 1 && this.col != 0 && this.col != xBound - 1) {
      this.neighbors.add(board.get(this.row).get(this.col - 1));
      this.neighbors.add(board.get(this.row).get(this.col + 1));
      this.neighbors.add(board.get(this.row - 1).get(this.col));
    }

    else if (this.row != 0 && this.row != yBound - 1 && this.col == xBound - 1) {
      this.neighbors.add(board.get(this.row - 1).get(this.col));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
      this.neighbors.add(board.get(this.row).get(this.col - 1));
    }

    else {
      this.neighbors.add(board.get(this.row - 1).get(this.col));
      this.neighbors.add(board.get(this.row).get(this.col - 1));
      this.neighbors.add(board.get(this.row).get(this.col + 1));
      this.neighbors.add(board.get(this.row + 1).get(this.col));
    }
  }

  // set all possible edges related to this gamepiece position to a list
  ArrayList<Edge> setEdge(ArrayList<Edge> allPath, ArrayList<ArrayList<GamePiece>> board,
      int xBound, int yBound, Random rand) {
    if (this.col > 0) {
      allPath.add(new Edge(this, board.get(row).get(col - 1), rand));
    }
    if (this.col < xBound - 1) {
      allPath.add(new Edge(this, board.get(row).get(col + 1), rand));
    }
    if (row > 0) {
      allPath.add(new Edge(this, board.get(row - 1).get(col), rand));
    }
    if (row < yBound - 1) {
      allPath.add(new Edge(this, board.get(row + 1).get(col), rand));
    }
    return allPath;
  }

  // checks if the two pieces are linked
  boolean linkedTo(GamePiece other) {
    if (other.row == this.row) {
      if (other.col == this.col + 1) {
        if (other.left && this.right) {
          return true;
        }
      }
      if (other.col == this.col - 1) {
        if (other.right && this.left) {
          return true;
        }
      }
    }
    if (other.col == this.col) {
      if (other.row == this.row + 1) {
        if (other.top && this.bottom) {
          return true;
        }
      }
      if (other.row == this.row - 1) {
        if (other.bottom && this.top) {
          return true;
        }
      }
    }
    return false;
  }

  // updates the radius to every connected pieces
  void radiusUpdate() {
    for (GamePiece g : this.neighbors) {
      if (this.linkedTo(g)) {
        this.connected.add(g);
      }
    }
  }

}

// represents an edge
class Edge {
  GamePiece fromNode;
  GamePiece toNode;
  int weight;
  Random rand;

  // default constructor
  Edge(GamePiece fromNode, GamePiece toNode, Random rand) {
    this.rand = rand;
    this.fromNode = fromNode;
    this.toNode = toNode;
    this.weight = this.rand.nextInt(100);
  }
}

// sorting standard by weight
class SortByWeight implements Comparator<Edge> {
  // Used for sorting in ascending order by weight
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

// represents the world
class LightEmAll extends World {
  // a list of columns of GamePieces,
  // i.e., represents the board in column-major order
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // a list of edges of the minimum spanning tree
  ArrayList<Edge> mst;

  // the width and height of the board
  int width;
  int height;
  // the current location of the power station,
  // as well as its effective radius

  // Whistle : accumulates the amount of clicks the player has in a game
  int clicksCount;
  // Whistle : accumulates the time passed in ticks
  int time;
  int powerRow;
  int powerCol;
  int radius;
  Random rand;

  // default constructor
  LightEmAll(int width, int height) {
    this.nodes = new ArrayList<GamePiece>();
    this.mst = new ArrayList<Edge>();
    this.width = width;
    this.height = height;
    this.rand = new Random();
    this.clicksCount = 0;
    this.time = 0;
    this.powerCol = 0;
    this.powerRow = 0;
    this.radius = 0;

    this.board = this.initBoard();
    this.board.get(powerRow).get(powerCol).powerStation = true;
    this.initNode();
    this.initNeighbors();
    this.initTree();
    this.initKruskal();
    this.initRadius();
    this.shuffle();
  }

  // testing constructor
  LightEmAll(ArrayList<ArrayList<GamePiece>> board) {
    this.board = board;
    this.nodes = new ArrayList<GamePiece>();
    this.mst = new ArrayList<Edge>();
    this.width = board.get(0).size();
    this.height = board.size();
    this.rand = new Random();
    this.clicksCount = 0;
    this.time = 0;
    this.powerCol = 0;
    this.powerRow = 0;
    this.radius = 0;
    this.board.get(powerRow).get(powerCol).powerStation = true;
    this.initNode();
    this.initNeighbors();
    this.initTree();
    this.initRadius();
  }

  // initializes grid with board represented in rows and columns
  ArrayList<ArrayList<GamePiece>> initBoard() {
    ArrayList<GamePiece> col = new ArrayList<GamePiece>(this.width);
    ArrayList<ArrayList<GamePiece>> rows = new ArrayList<ArrayList<GamePiece>>(this.height);

    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        col.add(new GamePiece(i, j));
      }
      rows.add(col);
      col = new ArrayList<GamePiece>(this.width);
    }
    return rows;
  }

  // initializes a list of all nodes in the grid
  void initNode() {
    for (ArrayList<GamePiece> list : board) {
      for (GamePiece g : list) {
        this.nodes.add(g);
      }
    }
  }

  // checks if the number is even
  public boolean evenp(int num) {
    return (num % 2) == 0;
  }

  // initializes the Kruskal's random board
  void initTree() {
    ArrayList<Edge> allPath = new ArrayList<Edge>();
    HashMap<Posn, Posn> rep = this.getHash();

    // inputting all the potential edges into allPath
    for (GamePiece g : this.nodes) {
      allPath = g.setEdge(allPath, this.board, this.width, this.height, this.rand);
    }

    // sort the list of all potential edges by ascending order
    Collections.sort(allPath, new SortByWeight());

    // creating the minimum spanning tree
    while (this.moreThanOneTree(rep)) {
      Edge next = allPath.remove(0);
      Posn toNode = new Posn(next.toNode.col, next.toNode.row);
      Posn fromNode = new Posn(next.fromNode.col, next.fromNode.row);

      if (!(this.find(fromNode, rep).equals(this.find(toNode, rep)))) {
        this.mst.add(next);
        rep.put(this.find(fromNode, rep), toNode);
      }
    }
  }

  // follows the links in the rep map and recursively look up the rep for the
  // current node's parent if it doesn't map to itself.
  Posn find(Posn p, HashMap<Posn, Posn> rep) {
    if (rep.get(p).equals(p)) {
      return p;
    }
    else {
      return this.find(rep.get(p), rep);
    }
  }

  // checks if there are more than one tree in the hashmap
  boolean moreThanOneTree(HashMap<Posn, Posn> rep) {
    Posn base = this.find(new Posn(this.board.get(0).get(0).col, this.board.get(0).get(0).row),
        rep);
    for (GamePiece g : this.nodes) {
      if (!this.find(new Posn(g.col, g.row), rep).equals(base)) {
        return true;
      }
    }
    return false;
  }

  // maps the posn of each node to its node
  HashMap<Posn, Posn> getHash() {
    HashMap<Posn, Posn> rep = new HashMap<Posn, Posn>();
    for (GamePiece g : this.nodes) {
      Posn p = new Posn(g.col, g.row);
      rep.put(p, p);
    }
    return rep;
  }

  // create the wires based on the spanning tree
  void initKruskal() {
    for (Edge e : this.mst) {
      GamePiece toNode = e.toNode;
      GamePiece fromNode = e.fromNode;

      // toNode is on the right
      if (toNode.col - fromNode.col == 1) {
        toNode.left = true;
        fromNode.right = true;
      }
      // toNode is on the left
      else if (toNode.col - fromNode.col == -1) {
        toNode.right = true;
        fromNode.left = true;
      }
      // toNode is below
      else if (toNode.row - fromNode.row == 1) {
        toNode.top = true;
        fromNode.bottom = true;
      }
      // toNode is above
      else if (toNode.row - fromNode.row == -1) {
        toNode.bottom = true;
        fromNode.top = true;
      }
    }
  }

  /*
   * // Initializes the game board to have a connected fractal wiring void
   * initFractal() { this.fractalGenerate(0, 1, 0, 1, 2, 2); }
   * 
   * // generates a fractal structure based on the dimension of the grid // Keeps
   * track of the current size of the fractal and the corner positions of // the U
   * shape void fractalGenerate(int left, int right, int top, int bottom, int
   * curX, int curY) { if (this.width == 1 && this.height == 1) {
   * this.board.get(0).get(0).bottom = true; } else if (this.width == 2 &&
   * this.height == 1) { this.board.get(0).get(0).right = true;
   * this.board.get(0).get(1).left = true; } else if (this.width == 1 &&
   * this.height == 2) { this.board.get(0).get(0).bottom = true;
   * this.board.get(1).get(0).top = true; } else if (this.width == 0 &&
   * this.height == 0) { return; } else { this.setU(left, right, top, bottom); }
   * 
   * if (right == this.width - 1 && bottom == this.height - 1) { if (left > 0 ||
   * top > 0) { if (curX * 2 > this.width && curY * 2 > this.height) {
   * this.fractalGenerate(0, this.width - 1, 0, this.height - 1, this.width,
   * this.height); }
   * 
   * else if (curX * 2 > this.width) { this.fractalGenerate(0, this.width - 1, 0,
   * curY * 2 - 1, this.width, curY * 2); }
   * 
   * else if (curY * 2 >= this.height) { this.fractalGenerate(0, curX * 2 - 1, 0,
   * this.height - 1, curX * 2, this.height); }
   * 
   * else { this.fractalGenerate(0, curX * 2 - 1, 0, curY * 2 - 1, curX * 2, curY
   * * 2); } } }
   * 
   * else if (bottom == this.height - 1) { if (right + curX >= this.width) {
   * left--; right = this.width - 1 - curX; }
   * 
   * this.fractalGenerate(left + curX, right + curX, 0, curY - 1, curX, curY); }
   * 
   * else { if (bottom + curY >= this.height) { top--; bottom = this.height - 1 -
   * curY; } this.fractalGenerate(left, right, top + curY, bottom + curY, curX,
   * curY); } }
   * 
   * // Creates a U structure using the given indexes void setU(int leftCol, int
   * rightCol, int topRow, int bottomRow) { for (int row = topRow; row <=
   * bottomRow; row++) { for (int col = leftCol; col <= rightCol; col++) { if (col
   * == leftCol || col == rightCol) { if (row == topRow) {
   * this.board.get(row).get(col).bottom = true; }
   * 
   * else if (row == bottomRow) { this.board.get(row).get(col).top = true; }
   * 
   * else { this.board.get(row).get(col).top = true;
   * this.board.get(row).get(col).bottom = true; } }
   * 
   * if (row == bottomRow) { if (col == leftCol) {
   * this.board.get(row).get(col).right = true; }
   * 
   * else if (col == rightCol) { this.board.get(row).get(col).left = true; }
   * 
   * else { this.board.get(row).get(col).left = true;
   * this.board.get(row).get(col).right = true; } } } } }
   * 
   * // manually generates a maze void manualGenerate() { for (int i = 0; i <
   * this.height; i++) { for (int j = 0; j < this.width; j++) { if
   * (((evenp(this.height) && i == (this.height / 2)) || (!evenp(this.height) && i
   * == (this.height - 1) / 2)) && j == 0) {
   * this.board.get(i).get(j).setPiece(false, true, true, true); } else if
   * (((evenp(this.height) && i == (this.height / 2)) || (!evenp(this.height) && i
   * == (this.height - 1) / 2)) && j == (this.width - 1)) {
   * this.board.get(i).get(j).setPiece(true, false, true, true); } else if
   * (((evenp(this.height) && i == (this.height / 2)) || (!evenp(this.height) && i
   * == (this.height - 1) / 2)) && (j != 0) && (j != (this.width - 1))) {
   * this.board.get(i).get(j).setPiece(true, true, true, true); } else if (i == 0)
   * { this.board.get(i).get(j).setPiece(false, false, false, true); } else if (i
   * == this.height - 1) { this.board.get(i).get(j).setPiece(false, false, true,
   * false); } else if ((!evenp(this.height) && (i != (this.height - 1) / 2)) ||
   * (evenp(this.height) && (i != (this.height / 2)))) {
   * this.board.get(i).get(j).setPiece(false, false, true, true); } } } }
   */

  // initializing adding neighbors
  void initNeighbors() {
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        this.board.get(i).get(j).addNeighbors(this.board, this.width, this.height);
      }
    }
  }

  /*
   * // set the power station to the first row middle void setPower() { for (int
   * col = 0; col < this.width; col++) { if (evenp(this.width) && col ==
   * (this.width / 2)) { this.board.get(0).get(col).powerStation = true;
   * this.powerCol = col; this.powerRow = 0; } else if (!evenp(this.width) && col
   * == (this.height - 1) / 2) { this.board.get(0).get(col).powerStation = true;
   * this.powerCol = col; this.powerRow = 0; } } }
   */

  // initializes the power radius
  void initRadius() {
    this.radiusUpdate();
    GamePiece lastNode = this.getLast(board.get(powerRow).get(powerCol));
    this.radius = this.findDiameter(lastNode,
        this.getLast(board.get(lastNode.row).get(lastNode.col))) / 2 + 1;
  }

  // update the radius into every neighbors in the game
  void radiusUpdate() {
    for (GamePiece g : this.nodes) {
      g.radiusUpdate();
    }
  }

  // finds the furthest away gamepiece from the passed in gamepiece
  GamePiece getLast(GamePiece gp) {
    ArrayList<GamePiece> prev = new ArrayList<GamePiece>();
    ArrayList<GamePiece> queueList = new ArrayList<GamePiece>();

    queueList.add(this.board.get(gp.row).get(gp.col));

    while (queueList.size() > 0) {
      GamePiece gp2 = queueList.get(0);
      queueList.remove(0);

      if (!prev.contains(gp2)) {
        prev.add(gp2);
        for (GamePiece g : gp2.connected) {
          queueList.add(g);
        }
      }
    }
    return prev.get(prev.size() - 1);
  }

  // calculates the diameter of the graph
  int findDiameter(GamePiece toNode, GamePiece fromNode) {
    ArrayList<GamePiece> worklist = new ArrayList<GamePiece>();
    HashMap<GamePiece, Integer> cameFromEdge = new HashMap<GamePiece, Integer>();
    worklist.add(fromNode);
    cameFromEdge.put(fromNode, 0);

    while (worklist.size() > 0) {
      GamePiece next = worklist.get(0);
      worklist.remove(0);

      for (GamePiece g : next.connected) {
        int newCameFromEdge = cameFromEdge.get(next) + 1;
        if (cameFromEdge.get(g) == null || cameFromEdge.get(g) > newCameFromEdge) {
          cameFromEdge.put(g, newCameFromEdge);
          worklist.add(g);
        }
      }
    }
    if (cameFromEdge.get(toNode) == null) {
      return -1;
    }
    else {
      return cameFromEdge.get(toNode);
    }
  }

  // randomly rotates game pieces
  void shuffle() {
    for (GamePiece g : this.nodes) {
      for (int i = 0; i <= rand.nextInt(4); i++) {
        g.rotate();
      }
    }
  }

  // on tick state
  public void onTick() {
    this.radiusUpdate();
    this.shutLight();
    this.openLight();
    this.time++;
  }

  // mouse click state
  public void onMouseClicked(Posn pos, String buttonName) {
    int row = (int) (Math.floor(pos.y / Constants.PIECE_SIZE));
    int column = (int) (Math.floor(pos.x / Constants.PIECE_SIZE));

    if (buttonName.equals("LeftButton")) {
      this.board.get(row).get(column).rotate();
      this.clicksCount++;
    }
    // Whistle: counter rotate a tile when right click
    if (buttonName.equals("RightButton")) {
      this.board.get(row).get(column).counterRotate();
      this.clicksCount++;
    }
    this.shutLight();
    this.powerUp(board.get(powerRow).get(powerCol), radius);
  }

  // checks if the two game pieces are connected
  public boolean connected(GamePiece g1, GamePiece g2) {
    if (g1.row - g2.row == 1 && g1.top && g2.bottom) {
      return true;
    }
    if (g2.row - g1.row == 1 && g1.bottom && g2.top) {
      return true;
    }
    if (g2.col - g1.col == 1 && g1.right && g2.left) {
      return true;
    }
    return (g1.col - g2.col == 1 && g1.left && g2.right);
  }

  // power up all game pieces within the radius
  public void openLight() {
    this.powerUp(board.get(powerRow).get(powerCol), this.radius);
  }

  // power up the nearby radius of the game piece
  public void powerUp(GamePiece g, int radius) {
    g.power = true;
    int row = g.row;
    int col = g.col;
    if (radius == 0) {
      return;
    }
    if (row - 1 >= 0) {
      if (g.top && this.board.get(row - 1).get(col).bottom
          && !this.board.get(row - 1).get(col).power) {
        this.board.get(row - 1).get(col).power = true;
        this.powerUp(this.board.get(row - 1).get(col), radius - 1);
      }
    }
    if (row + 1 < this.height) {
      if (g.bottom && this.board.get(row + 1).get(col).top
          && !this.board.get(row + 1).get(col).power) {
        this.board.get(row + 1).get(col).power = true;
        this.powerUp(this.board.get(row + 1).get(col), radius - 1);
      }
    }
    if (col - 1 >= 0) {
      if (g.left && this.board.get(row).get(col - 1).right
          && !this.board.get(row).get(col - 1).power) {
        this.board.get(row).get(col - 1).power = true;
        this.powerUp(this.board.get(row).get(col - 1), radius - 1);
      }
    }
    if (col + 1 < this.width) {
      if (g.right && this.board.get(row).get(col + 1).left
          && !this.board.get(row).get(col + 1).power) {
        this.board.get(row).get(col + 1).power = true;
        this.powerUp(this.board.get(row).get(col + 1), radius - 1);
      }
    }
  }

  // turn off all lights
  public void shutLight() {
    for (int i = 0; i < this.board.size(); i++) {
      for (int j = 0; j < this.board.get(i).size(); j++) {
        this.board.get(i).get(j).power = false;
      }
    }
  }

  // key press state
  public void onKeyEvent(String key) {
    if (key.equals("up") && powerRow != 0 && this.connected(board.get(powerRow).get(powerCol),
        board.get(powerRow - 1).get(powerCol))) {
      board.get(powerRow).get(powerCol).powerStation = false;
      this.powerRow = powerRow - 1;
      board.get(powerRow).get(powerCol).powerStation = true;
    }
    if (key.equals("right") && powerCol != this.width - 1 && this
        .connected(board.get(powerRow).get(powerCol), board.get(powerRow).get(powerCol + 1))) {
      board.get(powerRow).get(powerCol).powerStation = false;
      this.powerCol = powerCol + 1;
      board.get(powerRow).get(powerCol).powerStation = true;
    }
    if (key.equals("down") && powerRow != this.height - 1 && this
        .connected(board.get(powerRow).get(powerCol), board.get(powerRow + 1).get(powerCol))) {
      board.get(powerRow).get(powerCol).powerStation = false;
      this.powerRow = powerRow + 1;
      board.get(powerRow).get(powerCol).powerStation = true;
    }
    if (key.equals("left") && powerCol != 0 && this.connected(board.get(powerRow).get(powerCol),
        board.get(powerRow).get(powerCol - 1))) {
      board.get(powerRow).get(powerCol).powerStation = false;
      this.powerCol = powerCol - 1;
      board.get(powerRow).get(powerCol).powerStation = true;
    }
    // Whistle : solving the game for player (aka press f to pay respect)
    if (key.equals("f")) {
      this.solveTree();
    }
    // Whistle : start a new puzzle without restarting the program
    if (key.equals("r")) {
      this.restart();
    }
  }

  // solves the shuffled wires automatically
  void solveTree() {
    this.wireDisconnect();
    this.initKruskal();
  }

  // removes every wire from the board
  void wireDisconnect() {
    for (GamePiece g : this.nodes) {
      g.setPiece(false, false, false, false);
    }
  }

  // restart the puzzle without restarting the program
  void restart() {
    this.clicksCount = 0;
    this.time = 0;
    this.nodes = new ArrayList<GamePiece>();
    this.mst = new ArrayList<Edge>();
    this.board = this.initBoard();
    this.powerCol = 0;
    this.powerRow = 0;
    this.board.get(powerRow).get(powerCol).powerStation = true;
    this.initNode();
    this.initNeighbors();
    this.initTree();
    this.initKruskal();
    this.initRadius();
    this.shuffle();
  }

  // checks if all power are on
  boolean allLit() {
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        if (!this.board.get(i).get(j).power) {
          return false;
        }
      }
    }
    return true;
  }

  // end the game when the condition is fulfilled
  // wins the game when all wires are connected and powered
  // loses the game when number of clicks exceeds three times the number
  // of cells without winning the game
  public WorldEnd worldEnds() {
    if (this.allLit()) {
      return new WorldEnd(true, this.lastScene("You Win!!"));
    }
    if (this.clicksCount > this.width * this.height * 5) {
      return new WorldEnd(true, this.lastScene("You Lose"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // draws a winning or losing game scene when prompted
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.makeScene();
    TextImage a = new TextImage(msg, 70, Color.red);
    TextImage b = new TextImage(msg, 70, Color.green);
    TextImage c = new TextImage("Time Spent (s): " + Integer.toString(this.time / 100), 40,
        Color.red);
    TextImage d = new TextImage("Number of Clicks: " + Integer.toString(this.clicksCount), 40,
        Color.red);

    if (msg.contains("Win")) {
      scene.placeImageXY(b, this.width * Constants.PIECE_SIZE / 2,
          this.height * Constants.PIECE_SIZE / 2);
      scene.placeImageXY(c, this.width * Constants.PIECE_SIZE / 2,
          this.height * Constants.PIECE_SIZE / 4);
      scene.placeImageXY(d, this.width * Constants.PIECE_SIZE / 2,
          (this.height * Constants.PIECE_SIZE / 4) * 3);
    }
    else if (msg.contains("Lose")) {
      scene.placeImageXY(a, this.width * Constants.PIECE_SIZE / 2,
          this.height * Constants.PIECE_SIZE / 2);
    }
    return scene;
  }

  // draws the game in entirety
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        this.board.get(i).get(j).drawAt(scene);
      }
    }
    return scene;
  }

}

//an examples class
class ExampleLightEmAll {
  GamePiece g1;
  GamePiece g2;
  GamePiece g3;
  GamePiece g4;
  GamePiece g5;
  GamePiece g6;
  GamePiece g7;
  GamePiece g8;
  GamePiece g9;

  ArrayList<GamePiece> a1;
  ArrayList<GamePiece> a2;
  ArrayList<GamePiece> a3;

  ArrayList<ArrayList<GamePiece>> board;

  WorldScene background1;
  WorldScene background2;
  WorldScene background3;
  WorldScene background4;
  WorldScene background5;
  WorldScene background6;
  WorldScene background7;
  RectangleImage tile1;
  RectangleImage line1;
  RectangleImage line2;
  RectangleImage outline1;
  StarImage power1;

  LightEmAll state1;
  LightEmAll state2;
  LightEmAll state3;

  GamePiece topLeft;
  GamePiece topMid;
  GamePiece topRight;
  GamePiece midLeft;
  GamePiece midMid;
  GamePiece midRight;
  GamePiece bottomLeft;
  GamePiece bottomMid;
  GamePiece bottomRight;

  ArrayList<GamePiece> top;
  ArrayList<GamePiece> mid;
  ArrayList<GamePiece> bottom;

  ArrayList<ArrayList<GamePiece>> board2;

  void initExamples() {
    this.g1 = new GamePiece(0, 0);
    this.g2 = new GamePiece(0, 1);
    this.g3 = new GamePiece(0, 2);
    this.g4 = new GamePiece(1, 0);
    this.g5 = new GamePiece(1, 1);
    this.g6 = new GamePiece(1, 2);
    this.g7 = new GamePiece(2, 0);
    this.g8 = new GamePiece(2, 1);
    this.g9 = new GamePiece(2, 2);

    this.a1 = new ArrayList<GamePiece>(Arrays.asList(this.g1, this.g2, this.g3));
    this.a2 = new ArrayList<GamePiece>(Arrays.asList(this.g4, this.g5, this.g6));
    this.a3 = new ArrayList<GamePiece>(Arrays.asList(this.g7, this.g8, this.g9));

    this.board = new ArrayList<ArrayList<GamePiece>>(Arrays.asList(this.a1, this.a2, this.a3));

    this.background1 = new WorldScene(300, 300);
    this.background2 = new WorldScene(300, 300);
    this.background3 = new WorldScene(300, 300);
    this.background4 = new WorldScene(300, 300);
    this.background5 = new WorldScene(300, 300);
    this.background6 = new WorldScene(300, 300);
    this.background7 = new WorldScene(300, 300);
    this.tile1 = new RectangleImage(100, 100, "solid", Color.darkGray);
    this.line1 = new RectangleImage(57, 14, "solid", Color.gray);
    this.line2 = new RectangleImage(14, 57, "solid", Color.gray);
    this.power1 = new StarImage(20, OutlineMode.SOLID, Color.cyan);
    this.outline1 = new RectangleImage(100, 100, "outline", Color.black);

    this.background3.placeImageXY(this.tile1, 50, 50);
    this.background3.placeImageXY(this.line1, 29, 50);
    this.background3.placeImageXY(this.outline1, 50, 50);

    this.background4.placeImageXY(this.tile1, 50, 50);
    this.background4.placeImageXY(this.line1, 71, 50);
    this.background4.placeImageXY(this.outline1, 50, 50);

    this.background5.placeImageXY(this.tile1, 50, 50);
    this.background5.placeImageXY(this.line2, 50, 29);
    this.background5.placeImageXY(this.outline1, 50, 50);

    this.background6.placeImageXY(this.tile1, 50, 50);
    this.background6.placeImageXY(this.line2, 50, 71);
    this.background6.placeImageXY(this.outline1, 50, 50);

    this.background7.placeImageXY(this.tile1, 50, 50);
    this.background7.placeImageXY(this.outline1, 50, 50);
    this.background7.placeImageXY(this.power1, 50, 50);

    this.state1 = new LightEmAll(3, 3);
    this.state2 = new LightEmAll(3, 3);

    topLeft = new GamePiece(0, 0, false, true, false, true);
    topMid = new GamePiece(0, 1, true, true, false, true);
    topRight = new GamePiece(0, 2, true, false, false, true);

    top = new ArrayList<GamePiece>(Arrays.asList(topLeft, topMid, topRight));

    midLeft = new GamePiece(1, 0, false, false, true, true);
    midMid = new GamePiece(1, 1, false, false, true, true);
    midRight = new GamePiece(1, 2, false, false, true, true);

    mid = new ArrayList<GamePiece>(Arrays.asList(midLeft, midMid, midRight));

    bottomLeft = new GamePiece(2, 0, false, true, true, false);
    bottomMid = new GamePiece(2, 1, true, true, true, false);
    bottomRight = new GamePiece(2, 2, true, false, true, false);

    bottom = new ArrayList<GamePiece>(Arrays.asList(bottomLeft, bottomMid, bottomRight));

    board2 = new ArrayList<ArrayList<GamePiece>>(Arrays.asList(top, mid, bottom));

    state3 = new LightEmAll(board2);
  }

  void testSetPieceAndRotate(Tester t) {
    this.initExamples();

    t.checkExpect(this.g1.left, false);
    t.checkExpect(this.g1.right, false);
    t.checkExpect(this.g1.top, false);
    t.checkExpect(this.g1.bottom, false);

    this.g1.setPiece(true, true, false, true);

    t.checkExpect(this.g1.left, true);
    t.checkExpect(this.g1.right, true);
    t.checkExpect(this.g1.top, false);
    t.checkExpect(this.g1.bottom, true);

    this.g1.rotate();

    t.checkExpect(this.g1.left, true);
    t.checkExpect(this.g1.right, false);
    t.checkExpect(this.g1.top, true);
    t.checkExpect(this.g1.bottom, true);
  }

  void testAddNeighbors(Tester t) {
    this.initExamples();
    // first case
    t.checkExpect(this.g1.neighbors.size(), 0);
    this.g1.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g1.neighbors.size(), 2);

    this.initExamples();
    // second case
    t.checkExpect(this.g2.neighbors.size(), 0);
    this.g2.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g2.neighbors.size(), 3);

    this.initExamples();
    // third case
    t.checkExpect(this.g3.neighbors.size(), 0);
    this.g3.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g3.neighbors.size(), 2);

    this.initExamples();
    // fourth case
    t.checkExpect(this.g4.neighbors.size(), 0);
    this.g4.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g4.neighbors.size(), 3);

    this.initExamples();
    // fifth case
    t.checkExpect(this.g5.neighbors.size(), 0);
    this.g5.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g5.neighbors.size(), 4);

    this.initExamples();
    // sixth case
    t.checkExpect(this.g6.neighbors.size(), 0);
    this.g6.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g6.neighbors.size(), 3);

    this.initExamples();
    // seventh case
    t.checkExpect(this.g7.neighbors.size(), 0);
    this.g7.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g7.neighbors.size(), 2);

    this.initExamples();
    // eighth case
    t.checkExpect(this.g8.neighbors.size(), 0);
    this.g8.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g8.neighbors.size(), 3);

    this.initExamples();
    // ninth case
    t.checkExpect(this.g9.neighbors.size(), 0);
    this.g9.addNeighbors(this.board, 3, 3);
    t.checkExpect(this.g9.neighbors.size(), 2);
  }

  void testDrawAt(Tester t) {
    // first condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.g1.setPiece(true, false, false, false);
    this.g1.drawAt(this.background1);

    t.checkExpect(this.background1, this.background3);

    // second condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.g1.setPiece(false, true, false, false);
    this.g1.drawAt(this.background1);

    t.checkExpect(this.background1, this.background4);

    // third condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.g1.setPiece(false, false, true, false);
    this.g1.drawAt(this.background1);

    t.checkExpect(this.background1, this.background5);

    // fourth condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.g1.setPiece(false, false, false, true);
    this.g1.drawAt(this.background1);

    t.checkExpect(this.background1, this.background6);

    // fifth condition
    this.initExamples();

    t.checkExpect(this.background1, this.background2);

    this.g1.powerStation = true;
    this.g1.drawAt(this.background1);

    t.checkExpect(this.background1, this.background7);
  }

  void testEvenp(Tester t) {
    this.initExamples();

    t.checkExpect(this.state1.evenp(0), true);
    t.checkExpect(this.state1.evenp(2), true);
    t.checkExpect(this.state1.evenp(3), false);
  }

  void testKeyEvent(Tester t) {
    this.initExamples();

    state3.onKeyEvent("left");
    t.checkExpect(state3.powerCol, 0);

    state3.onKeyEvent("up");
    t.checkExpect(state3.powerRow, 0);

    state3.onKeyEvent("right");
    t.checkExpect(state3.powerCol, 1);

    state3.onKeyEvent("down");
    t.checkExpect(state3.powerRow, 1);

    state3.onKeyEvent("right");
    t.checkExpect(state3.powerCol, 1);

    state3.onKeyEvent("left");
    t.checkExpect(state3.powerCol, 1);

    state3.onKeyEvent("up");
    t.checkExpect(state3.powerRow, 0);

    state3.onKeyEvent("right");
    state3.onKeyEvent("down");
    t.checkExpect(state3.powerRow, 1);
    t.checkExpect(state3.powerCol, 2);

    state3.onKeyEvent("r");
    t.checkExpect(state3.powerRow, 0);
    t.checkExpect(state3.powerCol, 0);
  }

  void testMouseClicked(Tester t) {
    this.initExamples();
    int row = (int) (Math.floor(0 / Constants.PIECE_SIZE));
    int column = (int) (Math.floor(230 / Constants.PIECE_SIZE));

    t.checkExpect(state3.board.get(row).get(column), topRight);

    t.checkExpect(topRight.left, true);
    t.checkExpect(topRight.right, false);
    t.checkExpect(topRight.top, false);
    t.checkExpect(topRight.bottom, true);

    // clicked on piece, so should rotate
    state3.onMouseClicked(new Posn(0, 230), "LeftButton");

    t.checkExpect(topRight.left, true);
    t.checkExpect(topRight.top, false);
    t.checkExpect(topRight.right, false);
    t.checkExpect(topRight.bottom, true);
  }

  void testSetEdge(Tester t) {
    this.initExamples();

    ArrayList<Edge> allPath = new ArrayList<Edge>();
    Random rand = new Random();
    t.checkExpect(allPath.size(), 0);

    topLeft.setEdge(allPath, board2, board2.get(0).size(), board2.size(), rand);

    t.checkExpect(allPath.size(), 2);
  }

  void testLinkedTo(Tester t) {
    this.initExamples();

    t.checkExpect(topLeft.right, true);
    t.checkExpect(topMid.left, true);

    t.checkExpect(topLeft.linkedTo(topMid), true);
  }

  void testRadiusUpdate(Tester t) {
    this.initExamples();

    t.checkExpect(topLeft.connected.size(), 2);

    topLeft.radiusUpdate();

    t.checkExpect(topLeft.connected.size(), 4);
  }

  void testInitBoard(Tester t) {
    this.initExamples();

    ArrayList<ArrayList<GamePiece>> board3x3 = state1.initBoard();

    t.checkExpect(board3x3.size(), 3);
    t.checkExpect(board3x3.get(0).size(), 3);
  }

  void testInitNode(Tester t) {
    this.initExamples();

    for (ArrayList<GamePiece> row : state3.board) {
      t.checkExpect(state3.nodes.containsAll(row), true);
    }
  }

  void testInitTree(Tester t) {
    this.initExamples();

    state3.mst = new ArrayList<Edge>();
    t.checkExpect(state3.mst.size(), 0);

    state3.initTree();
    t.checkExpect(state3.mst.size() > 0, true);
  }

  void testOpenLightShutLight(Tester t) {
    this.initExamples();

    topLeft.power = false;
    t.checkExpect(topLeft.power, false);
    state3.openLight();
    t.checkExpect(topLeft.power, true);
    state3.shutLight();
    t.checkExpect(topLeft.power, false);
  }

  // big bang initializer
  void testGame(Tester t) {
    LightEmAll world = new LightEmAll(7, 7);
    world.bigBang(world.width * Constants.PIECE_SIZE, world.height * Constants.PIECE_SIZE,
        Constants.TICK_RATE);
  }
}
