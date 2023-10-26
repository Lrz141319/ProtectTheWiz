package WizardTD.Components;

import WizardTD.App;
import WizardTD.Managers.TowerManager;

public class BuildTowerButton extends Button
{
    private static final String m_LabelTxt = "T";
    private App m_App;
    private TowerManager m_TowerManager;
    private static final int m_PosX = 645;
    private static final int m_PosY = 135;

    public BuildTowerButton(App app, TowerManager towerManager)
    {
        super(app, m_PosX, m_PosY, m_LabelTxt, "Build tower");
        this.m_App = app;
        this.m_TowerManager = towerManager;
    }

    @Override
    public void OnPressed(int pressType)
    {
        super.OnPressed(pressType);
        if(super.m_IsBtnSelected)
        {
            m_TowerManager.EnterTowerPlaceMode();
        }
        else
        {
            m_TowerManager.LeaveTowerPlaceMode();
        }

    }

}
