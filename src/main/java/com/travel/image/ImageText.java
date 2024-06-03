package com.travel.image;


import java.io.Serializable;


public class ImageText implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String text;
    private ImageTextConfig imageTextConfig = new ImageTextConfig();

    public ImageText(String text)
    {

	this.text = text;
    }


    public ImageText(String text, ImageTextConfig imageTextConfig)
    {

	this.text = text;
	this.imageTextConfig = imageTextConfig;
    }


    public String getText()
    {

	return text;
    }


    public void setText(String text)
    {

	this.text = text;
    }


    public ImageTextConfig getImageTextConfig()
    {

	return imageTextConfig;
    }


    public void setImageTextConfig(ImageTextConfig imageTextConfig)
    {

	this.imageTextConfig = imageTextConfig;
    }

}
