package WizardTD.Managers;

import WizardTD.App;
import WizardTD.Objects.Enemy;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;

/*
This class manage the waves, controlling the wave flows,
spawning enemies for each waves, doing operations that
about the waves.
 */
public class WaveManager
{
    private App m_App;
    private JSONObject m_Config;
    private JSONArray m_Waves;
    private int m_CurrentWave = -1;
    public int CurrentWave() {return this.m_CurrentWave;}
    private float m_WaveDuration;
    public float WaveDuration() {return this.m_WaveDuration;}
    private ArrayList<Enemy> m_Enemies;
    public ArrayList<Enemy> Enemies() {return this.m_Enemies;}
    private int m_EnemySpawnDelay = 1000;
    private int m_LastSpawnTime = 0;

    public WaveManager(App app, JSONObject config)
    {
        m_Enemies = new ArrayList<>();
        this.m_App = app;
        this.m_Config = config;
        m_Waves = m_Config.getJSONArray("waves");
    }
    public void Update()
    {
        if(m_CurrentWave != -1 && m_App.IsGameRunning())
        {
            if(m_WaveDuration > 0)
            {
                m_WaveDuration -= 1 / m_App.frameRate * m_App.TimeScale();
            }
            else
            {
                if(m_CurrentWave < m_Waves.size() - 1)
                {
                    this.StartNextWave();
                }
                else
                {
                    m_WaveDuration = 0;
                    m_App.SetIsLevelEnded(true);
                }
            }
        }
    }
    public void StartNextWave()
    {
        m_CurrentWave++;
        JSONArray monsterConfig = m_Waves.getJSONObject(m_CurrentWave).getJSONArray("monsters");
        JSONObject waveObject = m_Waves.getJSONObject(m_CurrentWave);

        for(int i = 0; i < monsterConfig.size(); i++)
        {
            int hp = monsterConfig.getJSONObject(i).getInt("hp");
            float speed = monsterConfig.getJSONObject(i).getFloat("speed");
            float armour = monsterConfig.getJSONObject(i).getFloat("armour");
            int manaGain = monsterConfig.getJSONObject(i).getInt("mana_gained_on_kill");
            int quantity = monsterConfig.getJSONObject(i).getInt("quantity");
            SpawnEnemies(hp, speed, armour, manaGain, quantity);
        }
        m_WaveDuration = waveObject.getFloat("duration") + waveObject.getFloat("pre_wave_pause");
    }
    private void SpawnEnemies(int hp, float speed, float armour, int manaGain, int quantity)
    {
        PVector[] waypoints = m_App.Waypoints().toArray(new PVector[0]);
        if(waypoints.length > 0)
        {
            for(int i = 0; i < quantity; i++)
            {
                m_Enemies.add(new Enemy(waypoints[0].x, waypoints[0].y, m_App.Waypoints(), hp, speed, armour, manaGain, m_App));
            }
        }
        else
        {
            System.out.println("No waypoints");
        }
    }
}
