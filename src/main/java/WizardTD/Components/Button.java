package WizardTD.Components;


import WizardTD.App;

/*
This is a packaged class for buttons to inherit,
by inherit this class, the buttons will have basic display,
onclick listener functionality.
 */
public abstract class Button
{
    private int m_PosX;
    private int m_PosY;
    private static final int m_Width = 40;
    private static final int m_Height = 40;
    private String m_LabelTxt;
    private String m_DescriptionTxt;
    private App m_App;
    protected Boolean m_IsBtnSelected;
    protected Boolean m_IsHighlighted;
    private enum ButtonState
    {
        Idle,
        Highlighted,
        Selected
    }
    private ButtonState m_BtnState;

    public Button(App app,int x, int y, String labelTxt, String descTxt)
    {
        this.m_App = app;
        this.m_PosX = x;
        this.m_PosY = y;
        this.m_LabelTxt = labelTxt;
        this.m_DescriptionTxt = descTxt;
        this.m_IsBtnSelected = false;
        m_BtnState = ButtonState.Idle;
    }

    public void DisplayButton()
    {
        this.CheckMouseState();
        switch(m_BtnState)
        {
            case Idle:
                m_App.fill(255);
                break;
            case Highlighted:
                m_App.fill(160, 160, 160);
                break;
            case Selected:
                m_App.fill(204, 204, 0);
                break;
            default:
                break;
        }
        m_App.rect(m_PosX, m_PosY, m_Width, m_Height, 5);

        m_App.textAlign(m_App.CENTER,m_App.CENTER);
        m_App.fill(0);
        m_App.text(m_LabelTxt, m_PosX + m_Width / 2, m_PosY + m_Height / 2);

        m_App.textAlign(m_App.CENTER, m_App.CENTER);
        m_App.fill(0);
        m_App.textSize(12);
        m_App.text(m_DescriptionTxt, m_PosX + 75, m_PosY + m_Height / 2);
    }
    private void CheckMouseState()
    {
       m_IsHighlighted = m_App.mouseX >= m_PosX && m_App.mouseX <= m_PosX + m_Width &&
                m_App.mouseY >= m_PosY && m_App.mouseY <= m_PosY + m_Height;
        if(m_IsBtnSelected)
        {
            m_BtnState = ButtonState.Selected;
        }
        else if(m_IsHighlighted)
        {
            m_BtnState = ButtonState.Highlighted;
        }
        else
        {
            m_BtnState = ButtonState.Idle;
        }
    }
    protected void OnPressed(int pressType) // pressType: 0 = mouse, 1 = keyboard
    {
        if(pressType == 0)
        {
            CheckMouseState();
            if (m_IsHighlighted)
            {
                if (m_BtnState == ButtonState.Highlighted)
                {
                    if (!m_IsBtnSelected)
                    {
                        m_IsBtnSelected = true;
                    }
                }
                else if (m_BtnState == ButtonState.Selected)
                {
                    if (m_IsBtnSelected)
                    {
                        m_IsBtnSelected = false;
                    }
                }
            }
        }
        else
        {
            if(m_BtnState == ButtonState.Idle || m_BtnState == ButtonState.Highlighted)
            {
                if(!m_IsBtnSelected)
                {
                    m_IsBtnSelected = true;
                }
            }
            else if(m_BtnState == ButtonState.Selected)
            {
                if(m_IsBtnSelected)
                {
                    m_IsBtnSelected = false;
                }
            }
        }
    }
    protected void UpdateDescriptionImpl(String desc)
    {
        this.m_DescriptionTxt = desc;
    }
}
