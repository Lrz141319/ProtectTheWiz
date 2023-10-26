package WizardTD.Components;

import WizardTD.App;

public class FastForwardButton extends Button
{
    private static final String m_LabelTxt = "FF";
    private App m_App;
    private static final int m_PosX = 645;
    private static final int m_PosY = 45;
    public FastForwardButton(App app)
    {
        super(app, m_PosX, m_PosY, m_LabelTxt, "2x Speed");
        this.m_App = app;
    }
    @Override
    public void OnPressed(int pressType)
    {
        super.OnPressed(pressType);
        if(super.m_IsBtnSelected)
        {
            m_App.SetTimeScale(2);
        }
        else
        {
            m_App.SetTimeScale(1);
        }
    }
}
