package WizardTD;

import WizardTD.Components.BuildTowerButton;
import WizardTD.Components.FastForwardButton;
import WizardTD.Components.MPSButton;
import WizardTD.Components.PauseButton;
import WizardTD.Managers.TowerManager;
import WizardTD.Managers.WaveManager;
import WizardTD.Managers.WizardHouseManager;
import WizardTD.Objects.Enemy;
import WizardTD.Objects.Tower;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.util.*;

/*
This is the main game flow class, the game will follow the execution here
In this project, I am trying to not comment for functions/lines as I want
to provide readability by good namings.
 */
public class App extends PApplet
{

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;
    public static final int WIZHOUSESIZE = 48;

    public static int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH * CELLSIZE + TOPBAR;

    public static final int FPS = 60;

    public String configPath;
    private int m_TimeScale;
    public int TimeScale() {return m_TimeScale;}
    private boolean m_IsLevelEnded;
    public boolean IsLevelEnded() {return m_IsLevelEnded;}
    private long m_PreviousMillis;
    public Random random = new Random();
    public PImage GremlinImg;
    public PImage GrassImg;
    public PImage ShrubImg;
    public PImage WizHouseImg;
    private PImage m_Path0Img;
    private PImage m_Path1Img;
    private PImage m_Path2Img;
    private PImage m_Path3Img;
    private PImage m_Towerl1Img;
    private PImage m_Towerl2Img;
    private PImage m_Towerl3Img;
    private String[] m_LayoutData;
    private char[][] m_GameMap;
    private JSONObject m_Config;
    private boolean m_IsGameRunning;
    public boolean IsGameRunning() {return this.m_IsGameRunning;}
    private WaveManager m_WaveManager;
    private WizardHouseManager m_WizHouseManager;
    private TowerManager m_TowerManager;
    private MPSButton m_MPSbtn;
    private FastForwardButton m_FFbtn;
    private PauseButton m_Pausebtn;
    private BuildTowerButton m_BuildTowerbtn;
    private List<PVector> m_Waypoints;
    public List<PVector> Waypoints() {return this.m_Waypoints;}
    private PVector m_Wizardpoint;
    private List<PVector> m_Cornerpoints;
    private PVector m_Spawnpoint;
    private List<PVector> m_TowerPlaceTiles;
    public List<PVector> TowerPlaceTiles() {return this.m_TowerPlaceTiles;}

    public App()
    {
        this.configPath = "config.json";
    }

    @Override
    public void settings()
    {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup()
    {
        m_Config = loadJSONObject("config.json");
        m_Waypoints = new ArrayList<>();
        m_Cornerpoints = new ArrayList<>();
        m_TowerPlaceTiles = new ArrayList<>();
        m_WizHouseManager = new WizardHouseManager(this, this.m_Config);
        m_WaveManager = new WaveManager(this, this.m_Config);
        m_TowerManager = new TowerManager(this, this.m_WizHouseManager, this.m_Config);
        m_MPSbtn = new MPSButton(this,  m_WizHouseManager);
        m_FFbtn = new FastForwardButton(this);
        m_Pausebtn = new PauseButton(this);
        m_BuildTowerbtn = new BuildTowerButton(this, this.m_TowerManager);

        size(WIDTH, HEIGHT);
        frameRate(FPS);
        LoadImages();
        LoadMap();
        AddWayPoints();

        m_WaveManager.StartNextWave();

        m_TimeScale = 1;
        m_IsGameRunning = true;
    }

    @Override
    public void draw()
    {
        LoadMap();
        if(!m_IsLevelEnded)
        {
            UpdateGameLoop();
            UpdateUILoop();
        }
    }

    @Override
    public void keyPressed()
    {
        switch (key)
        {
            case 'p':
                m_Pausebtn.OnPressed(1);
                break;
            case 'f':
                m_FFbtn.OnPressed(1);
                break;
            case 'm':
                m_MPSbtn.OnPressed(1);
                break;
            case 't':
                m_BuildTowerbtn.OnPressed(1);
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        m_MPSbtn.OnPressed(0);
        m_FFbtn.OnPressed(0);
        m_Pausebtn.OnPressed(0);
        m_BuildTowerbtn.OnPressed(0);

        if(m_TowerManager.IsTowerPlaceMode())
        {
            if( m_TowerManager.IsValidPlacementTile())
            {
                m_TowerManager.PlaceTower();
            }
        }
    }

    private void UpdateGameLoop()
    {
        if(m_IsGameRunning)
        {
            m_WaveManager.Update();
            m_WizHouseManager.UpdateMana();
            DrawEnemies();
            DrawTowers();
        }
    }
    private void UpdateUILoop()
    {
        DrawBar();
        DrawButtons();
        if(m_IsGameRunning)
        {
            DrawWaveTxtUI();
            DrawManaBar();
        }
        else
        {
            DrawPausedTip();
        }
    }
    private void DrawEnemies()
    {
        for(int i = m_WaveManager.Enemies().size() - 1; i >= 0; i--)
        {
            Enemy enemy = m_WaveManager.Enemies().get(i);
            enemy.Move();
            image(GremlinImg, enemy.PosX(), enemy.PosY());
            if(enemy.CurrentWayPoint() >= m_Waypoints.size())
            {
                m_WaveManager.Enemies().remove(i);
            }
        }
    }
    private void DrawTowers()
    {
        for(Tower tower : m_TowerManager.PlacedTower())
        {
            tower.Display();
            image(m_Towerl1Img, tower.PosX(), tower.PosY());
        }
    }
    private void DrawWaveTxtUI()
    {
        fill(255, 255, 255);
        textSize(20);
        textAlign(LEFT);
        text("Wave: " + (m_WaveManager.CurrentWave() + 1) +
                " starts: " + nf(m_WaveManager.WaveDuration(), 0, 1), 10, 30);
    }
    private void DrawManaBar()
    {
        int barX = 420;
        int barY = 10;
        int barWidth = 300;
        int barHeight = 20;
        float filledWidth = (m_WizHouseManager.CurrentMana() / m_WizHouseManager.CurrentManaCap()) * barWidth;

        fill(255);
        rect(barX, barY, barWidth, barHeight, 10);

        fill(51, 153, 255);
        rect(barX, barY, filledWidth, barHeight, 10);

        fill(0);
        textSize(16);
        textAlign(CENTER, CENTER);
        text("Mana: " + nf(m_WizHouseManager.CurrentMana(), 0, -1) +
                " / " + nf(m_WizHouseManager.CurrentManaCap()),
                barX + (float) barWidth / 2, barY + (float) barHeight / 2);
    }
    private void DrawButtons()
    {
        m_MPSbtn.DisplayButton();
        m_FFbtn.DisplayButton();
        m_Pausebtn.DisplayButton();
        m_BuildTowerbtn.DisplayButton();
    }
    private void DrawPausedTip()
    {
        fill(255);
        textSize(20);
        textAlign(CENTER, CENTER);
        text("GAME PAUSED", 380, 20);
    }
    private void DrawBar()
    {
        fill(220, 20, 60);
        rect(0, 0, WIDTH, TOPBAR);

        fill(100, 149, 237);
        rect(WIDTH - SIDEBAR, TOPBAR, SIDEBAR, HEIGHT - TOPBAR);
    }
    public void SetTimeScale(int timeScale)
    {
        this.m_TimeScale = timeScale;
    }
    public void SetIsLevelEnded(boolean isEnded)
    {
        this.m_IsLevelEnded = isEnded;
    }
    public void SetIsGameRunning(boolean isRunning)
    {
        this.m_IsGameRunning = isRunning;
    }
    public static void main(String[] args)
    {
        PApplet.main("WizardTD.App");
    }

    private void LoadImages()
    {
        m_Towerl1Img = loadImage("src/main/resources/WizardTD/tower0.png");
        m_Towerl2Img = loadImage("src/main/resources/WizardTD/tower1.png");
        m_Towerl3Img = loadImage("src/main/resources/WizardTD/tower2.png");
        loadImage("src/main/resources/WizardTD/beetle.png");
        loadImage("src/main/resources/WizardTD/fireball.png");
        GrassImg = loadImage("src/main/resources/WizardTD/grass.png");
        GremlinImg = loadImage("src/main/resources/WizardTD/gremlin.png");
        loadImage("src/main/resources/WizardTD/gremlin1.png");
        loadImage("src/main/resources/WizardTD/gremlin2.png");
        loadImage("src/main/resources/WizardTD/gremlin3.png");
        loadImage("src/main/resources/WizardTD/gremlin4.png");
        loadImage("src/main/resources/WizardTD/gremlin5.png");
        m_Path0Img = loadImage("src/main/resources/WizardTD/path0.png");
        m_Path1Img = loadImage("src/main/resources/WizardTD/path1.png");
        m_Path2Img = loadImage("src/main/resources/WizardTD/path2.png");
        m_Path3Img = loadImage("src/main/resources/WizardTD/path3.png");
        ShrubImg = loadImage("src/main/resources/WizardTD/shrub.png");
        WizHouseImg = loadImage("src/main/resources/WizardTD/wizard_house.png");
        loadImage("src/main/resources/WizardTD/worm.png");

    }
    private void LoadMap()
    {
        String layoutFile = m_Config.getString("layout");
        m_LayoutData = loadStrings(layoutFile);
        m_GameMap = new char[m_LayoutData.length][m_LayoutData[0].length()];
        for (int i = 0; i < m_LayoutData.length; i++)
        {
            m_GameMap[i] = m_LayoutData[i].toCharArray();
        }
        boolean isFirstPath = true;
        for(int i = 0; i < m_GameMap.length; i++)
        {
            for (int j = 0; j < m_GameMap[i].length; j++)
            {
                float x = j * CELLSIZE;
                float y = i * CELLSIZE + TOPBAR;
                char cell = m_GameMap[i][j];
                switch(cell)
                {
                    case 'S':
                        image(ShrubImg, x, y, CELLSIZE, CELLSIZE);
                        break;
                    case 'X':
                        if(isFirstPath)
                        {
                            m_Spawnpoint = new PVector(x, y);
                            isFirstPath= false;
                        }
                        char leftCell = (j > 0) ? m_GameMap[i][j - 1] : ' ';
                        char rightCell = (j < m_GameMap[i].length - 1) ? m_GameMap[i][j + 1] : ' ';
                        char aboveCell = (i > 0) ? m_GameMap[i - 1][j] : ' ';
                        char belowCell = (i < m_GameMap.length - 1) ? m_GameMap[i + 1][j] : ' ';

                        if (aboveCell != 'X' && belowCell != 'X')
                        {
                            // Horizontal path
                            image(m_Path0Img, x, y, CELLSIZE, CELLSIZE);
                        }
                        else if (leftCell != 'X' && rightCell != 'X')
                        {
                            // Vertical path
                            PImage verticalPathImg = rotateImageByDegrees(m_Path0Img, 90);
                            image(verticalPathImg, x, y, CELLSIZE, CELLSIZE);
                        }
                        else if (leftCell == 'X' && rightCell == 'X' && aboveCell == 'X' && belowCell == 'X')
                        {
                            // Crossroad path
                            image(m_Path3Img, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell == 'X' && rightCell == 'X' && aboveCell != 'X' && belowCell == 'X')
                        {
                            // T path
                            image(m_Path2Img, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell == 'X' && rightCell == 'X' && aboveCell == 'X' && belowCell != 'X')
                        {
                            // T path facing top
                            PImage tUpPathImg = rotateImageByDegrees(m_Path2Img, 180);
                            image(tUpPathImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell == 'X' && rightCell != 'X' && aboveCell == 'X' && belowCell == 'X')
                        {
                            // T path facing left
                            PImage tLeftPathImg = rotateImageByDegrees(m_Path2Img, 90);
                            image(tLeftPathImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell != 'X' && rightCell == 'X' && aboveCell == 'X' && belowCell == 'X')
                        {
                            // T path facing right
                            PImage tRightPathImg = rotateImageByDegrees(m_Path2Img, -90);
                            image(tRightPathImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell == 'X' && belowCell == 'X' && aboveCell != 'X' && rightCell != 'X')
                        {
                            // Corner path left to bottom
                            image(m_Path1Img, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell == 'X' && aboveCell == 'X' && rightCell != 'X' && belowCell != 'X')
                        {
                            // Corner path left to top
                            PImage cornerLAImg = rotateImageByDegrees(m_Path1Img, 90);
                            image(cornerLAImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (leftCell != 'X' && belowCell !='X' && rightCell == 'X' && aboveCell == 'X')
                        {
                            // Corner path right to top
                            PImage cornerRAImg = rotateImageByDegrees(m_Path1Img, 180);
                            image(cornerRAImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        else if (rightCell == 'X' && belowCell == 'X' && leftCell != 'X' && aboveCell != 'X')
                        {
                            // Corner path right to bottom
                            PImage cornerRBImg = rotateImageByDegrees(m_Path1Img, -90);
                            image(cornerRBImg, x, y, CELLSIZE, CELLSIZE);
                            m_Cornerpoints.add(new PVector(x, y));
                        }
                        break;
                    case 'W':
                        m_Wizardpoint = new PVector(x, y);
                        image(GrassImg, x, y, CELLSIZE,CELLSIZE);
                        PImage roWizHoustImg = rotateImageByDegrees(WizHouseImg, -90);
                        image(roWizHoustImg, x - (WIZHOUSESIZE - CELLSIZE) , y - (WIZHOUSESIZE - CELLSIZE), WIZHOUSESIZE, WIZHOUSESIZE);
                        break;
                    default:
                        image(GrassImg, x, y, CELLSIZE, CELLSIZE);
                        m_TowerPlaceTiles.add(new PVector(x, y));
                        break;
                }
            }
        }
    }

    private void AddWayPoints()
    {
        m_Waypoints.add(m_Spawnpoint);
        for (int i = 0; i < m_Cornerpoints.size(); i++)
        {
            m_Waypoints.add(m_Cornerpoints.get(i));
        }
        m_Waypoints.add(m_Wizardpoint);
    }
    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     *
     * @param pimg  The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle)
    {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++)
        {
            for (int j = 0; j < newHeight; j++)
            {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
