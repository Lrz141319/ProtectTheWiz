package WizardTD.Managers;

import WizardTD.App;
import WizardTD.Objects.Tower;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
This class manage the Towers, such as placing towers, upgrading towers,
doing operations to tower objects
 */
public class TowerManager
{
    private App m_App;
    private JSONObject m_Config;
    private boolean m_IsTowerPlaceMode = false;
    public boolean IsTowerPlaceMode() {return this.m_IsTowerPlaceMode;}
    private PVector m_TowerPlaceTile;
    private List<Tower> m_PlacedTower;
    public List<Tower> PlacedTower() {return this.m_PlacedTower;}
    private WizardHouseManager m_WizHouseManager;
    public TowerManager(App app,WizardHouseManager wizardHouseManager, JSONObject config)
    {
        this.m_App = app;
        this.m_WizHouseManager = wizardHouseManager;
        this.m_Config = config;
        m_PlacedTower = new ArrayList<>();
    }

    public void EnterTowerPlaceMode()
    {
        m_IsTowerPlaceMode = true;
    }
    public void LeaveTowerPlaceMode()
    {
        m_IsTowerPlaceMode = false;
    }
    public void PlaceTower()
    {
        float towerRange = m_Config.getFloat("initial_tower_range");
        float firingSpeed = m_Config.getFloat("initial_tower_firing_speed");
        float towerDamage = m_Config.getFloat("initial_tower_damage");
        float posX = m_TowerPlaceTile.x;
        float posY = m_TowerPlaceTile.y;
        m_PlacedTower.add(new Tower(posX, posY, towerRange, firingSpeed, towerDamage, m_App));
        m_WizHouseManager.UpdateBuildTowerMana();
    }
    public boolean IsValidPlacementTile()
    {
        boolean tilesValid = false;
        for(PVector tiles : m_App.TowerPlaceTiles())
        {
            if(PApplet.dist(m_App.mouseX, m_App.mouseY, tiles.x, tiles.y) < 30)
            {
                tilesValid = true;
                m_TowerPlaceTile = new PVector(tiles.x, tiles.y);
                break;
            }
        }
        return tilesValid;
    }
}
