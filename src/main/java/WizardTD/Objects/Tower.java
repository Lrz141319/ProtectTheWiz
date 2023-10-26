package WizardTD.Objects;


import WizardTD.App;

/*
This is the Tower object class which represent each tower
in the game.
 */
public class Tower
{
    private float m_PosX;
    public float PosX(){return this.m_PosX;}
    private float m_PosY;
    public float PosY(){return this.m_PosY;}
    private App m_App;
    private float m_TowerRange;
    private float m_FiringSpeed;
    private float m_TowerDamage;
    private int m_Level;

    public Tower(float posX, float poxY, float towerRange, float firingSpeed, float towerDamage, App app)
    {
        this.m_PosX = posX;
        this.m_PosY = poxY;
        this.m_TowerRange = towerRange;
        this.m_FiringSpeed = firingSpeed;
        this.m_TowerDamage = towerDamage;
        this.m_App = app;
        this.m_Level = 1;
    }
    public void Display()
    {
        this.m_PosX = m_PosX;
        this.m_PosY = m_PosY;
    }
}
