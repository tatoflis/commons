package com.travel.image;


import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;


public class ImageTextConfig implements Serializable
{

    private static final long serialVersionUID = 1L;

    private Font textFont = new Font("Arial", Font.BOLD, 42);
    private Color textColor = Color.WHITE;

    public Font getTextFont()
    {

	return textFont;
    }


    public void setTextFont(Font textFont)
    {

	this.textFont = textFont;
    }


    public Color getTextColor()
    {

	return textColor;
    }


    public void setTextColor(Color textColor)
    {

	this.textColor = textColor;
    }

}
