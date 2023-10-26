package WizardTD.Components;

import WizardTD.App;
import WizardTD.Managers.WizardHouseManager;

public class MPSButton extends Button
{
    private WizardHouseManager m_WizHouseManager;
    private static final String m_LabelTxt = "M";
    private App m_App;
    private float m_ClickTime;
    private static final int m_PosX = 645;
    private static final int m_PosY = 315;

    public MPSButton(App app, WizardHouseManager wizardHouseManager)
    {
        super(app, m_PosX, m_PosY, m_LabelTxt, "Mana poll\ncost:100");
        this.m_WizHouseManager = wizardHouseManager;
        this.m_App = app;
    }
    @Override
    public void DisplayButton()
    {
        super.DisplayButton();
        float currentTime = m_App.millis();
        if(currentTime - m_ClickTime >= 150)
        {
            super.m_IsBtnSelected = false;
        }
    }
    @Override
    public void OnPressed(int pressType)
    {
        super.OnPressed(pressType);
        if(super.m_IsBtnSelected)
        {
            m_ClickTime = m_App.millis();
            m_WizHouseManager.UpdateManaCapMPS();
            UpdateDescription();
        }
    }

    private void UpdateDescription()
    {
        String descriptionTxt = "Mana pool\ncost:" + (int) m_WizHouseManager.MPSCurrentCost();
        super.UpdateDescriptionImpl(descriptionTxt);
    }
}
