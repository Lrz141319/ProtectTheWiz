package WizardTD.Managers;

import WizardTD.App;
import processing.data.JSONObject;

/*
This class manage the wizardhouse, doing operations about the wizard house
such as the mana pool spell operations, mana gain operations
 */
public class WizardHouseManager
{
    private App m_App;
    private JSONObject m_Config;
    private float m_CurrentMana;
    public float CurrentMana() { return m_CurrentMana;}
    private float m_CurrentManaCap;
    public float CurrentManaCap() { return m_CurrentManaCap;}
    private float m_CurrentManaGainedPerSec;
    private float m_MPSCurrentCost;
    public float MPSCurrentCost() { return m_MPSCurrentCost;}
    private float m_InitMana;
    private float m_InitManaCap;
    private float m_InitManaGainedPerSec;
    private float m_MPSInitCost;
    private float m_MPSCostIncresePerUse;
    private float m_MPSCapMultiplier;
    private float m_MPSManaGainedMultipler;
    private float m_TowerCost;

    public WizardHouseManager(App app, JSONObject config)
    {
        this.m_App = app;
        this.m_Config = config;
        m_InitMana = m_Config.getFloat("initial_mana");
        m_CurrentMana = m_InitMana;
        m_InitManaCap = m_Config.getFloat("initial_mana_cap");
        m_CurrentManaCap = m_InitManaCap;
        m_InitManaGainedPerSec = m_Config.getFloat("initial_mana_gained_per_second");
        m_CurrentManaGainedPerSec = m_InitManaGainedPerSec;
        m_MPSInitCost = m_Config.getFloat("mana_pool_spell_initial_cost");
        m_MPSCurrentCost = m_MPSInitCost;
        m_MPSCostIncresePerUse = m_Config.getFloat("mana_pool_spell_cost_increase_per_use");
        m_MPSCapMultiplier = m_Config.getFloat("mana_pool_spell_cap_multiplier");
        m_MPSManaGainedMultipler = m_Config.getFloat("mana_pool_spell_mana_gained_multiplier");
        m_TowerCost = m_Config.getFloat("tower_cost");
    }
    public void UpdateMana()
    {
        m_CurrentMana += m_CurrentManaGainedPerSec / m_App.frameRate * m_App.TimeScale();
    }
    public void UpdateManaCapMPS()
    {
        if(m_CurrentMana > m_MPSCurrentCost)
        {
            m_CurrentMana = m_CurrentMana - m_MPSCurrentCost;
            m_CurrentManaCap = m_CurrentManaCap * m_MPSCapMultiplier;
            m_MPSCurrentCost = m_MPSCurrentCost + m_MPSCostIncresePerUse;
        }
    }
    public void UpdateBuildTowerMana()
    {
        m_CurrentMana -= m_TowerCost;
    }
}
