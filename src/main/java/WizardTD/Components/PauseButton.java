package WizardTD.Components;

import WizardTD.App;

public class PauseButton extends Button
{
    private static final String m_LabelTxt = "P";
    private App m_App;
    private static final int m_PosX = 645;
    private static final int m_PosY = 90;

    public PauseButton(App app)
    {
        super(app, m_PosX, m_PosY, m_LabelTxt, "PAUSE");
        this.m_App = app;
    }
    @Override
    public void OnPressed(int pressType)
    {
        super.OnPressed(pressType);
        if(super.m_IsBtnSelected)
        {
            m_App.SetIsGameRunning(false);
            UpdateDescription("RESUME");
        }
        else
        {
            m_App.SetIsGameRunning(true);
            UpdateDescription("PAUSE");
        }
    }

    private void UpdateDescription(String desc)
    {
        super.UpdateDescriptionImpl(desc);
    }
}
