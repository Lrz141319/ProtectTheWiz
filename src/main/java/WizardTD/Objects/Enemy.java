package WizardTD.Objects;

import WizardTD.App;
import processing.core.PVector;

import java.util.List;

/*
This is the Enemy object class which
represent each enemies in game
 */
public class Enemy
{
    private float m_PosX;
    public float PosX() {return this.m_PosX;}
    private float m_PosY;
    public float PosY() {return this.m_PosY;}
    private List<PVector> m_Path;
    private int m_CurrentWayPoint;
    public int CurrentWayPoint() {return this.m_CurrentWayPoint;}
    private int m_Hp;
    private float m_Speed;
    private float m_Armour;
    private int m_ManaGain;
    private App m_App;


    public Enemy(float startX, float startY, List<PVector> waypoints, int hp, float speed, float armour, int manaGain, App app)
    {
        this.m_PosX = startX;
        this.m_PosY = startY;
        this.m_Path = waypoints;
        this.m_Hp = hp;
        this.m_Speed = speed;
        this.m_Armour = armour;
        this.m_ManaGain = manaGain;
        m_CurrentWayPoint = 0;
    }
    public void Move()
    {
        if(m_CurrentWayPoint < m_Path.size())
        {
            PVector target = m_Path.get(m_CurrentWayPoint);
            PVector dir = PVector.sub(target, new PVector(m_PosX, m_PosY));
            dir.normalize();
            dir.mult(m_Speed);
            m_PosX += dir.x;
            m_PosY += dir.y;

            float d = PVector.dist(new PVector(m_PosX, m_PosY), target);
            if(d < 1.0)
            {
                m_CurrentWayPoint++;
            }
        }
    }
}
