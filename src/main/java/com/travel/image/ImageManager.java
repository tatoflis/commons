package com.travel.image;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class ImageManager
{

    public static void main(String[] args) throws IOException
    {

	ImageManager imageManager = new ImageManager();

	InputStream imageInputStream = ImageManager.class.getResourceAsStream("/atardecer.png");
	// InputStream imageInputStream =
	// ImageManager.class.getResourceAsStream("/edificios.jpg");

	ImageText headerText = new ImageText("Agosto Costa Asturias");
	ImageText body1Text = new ImageText("Hotel 4* frente al mar");
	ImageText body2Text = new ImageText("cancelable por 36 €");

	ImageTextConfig imageFootTextConfig = new ImageTextConfig();
	imageFootTextConfig.setTextFont(new Font("Arial", Font.BOLD, 36));
	ImageText footText = new ImageText("www.exprimeviajes.com", imageFootTextConfig);

	InputStream generatedImage = imageManager.generateImage(imageInputStream, headerText, body1Text, body2Text, footText, 10, 40);

	// Guardar la imagen resultante
	ImageIO.write(ImageIO.read(generatedImage), "png", new File("C:\\Proyectos\\Travel\\wrk-travel\\travel-commons\\travel-utils\\src\\main\\resources\\nuevo.png"));

	System.out.println("Imagen con texto superpuesto creada correctamente.");
    }


    public InputStream generateImage(InputStream imageInputStream, ImageText header, ImageText body1, ImageText body2, ImageText foot, int lineSpacing, int extraLastLineSpacing)
    {

	Graphics2D g2d = null;

	try
	{

	    BufferedImage image = ImageIO.read(imageInputStream);

	    int width = image.getWidth();
	    int height = image.getHeight();

	    g2d = image.createGraphics();

	    // Configurar el texto
	    String[] lines =
	    { header.getText(), body1.getText(), body2.getText(), foot.getText() };
	    Font[] fonts =
	    { header.getImageTextConfig().getTextFont(), body1.getImageTextConfig().getTextFont(), body2.getImageTextConfig().getTextFont(), foot.getImageTextConfig().getTextFont() };
	    Color[] colors =
	    { header.getImageTextConfig().getTextColor(), body1.getImageTextConfig().getTextColor(), body2.getImageTextConfig().getTextColor(), foot.getImageTextConfig().getTextColor() };

	    generateImage(g2d, lines, fonts, colors, width, height, lineSpacing, extraLastLineSpacing);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, "png", baos);

	    System.out.println("Image generated!");
	    return new ByteArrayInputStream(baos.toByteArray());

	}
	catch (IOException e)
	{
	    e.printStackTrace();
	    throw new RuntimeException();
	}
	finally
	{
	    if(g2d != null)
	    {
		g2d.dispose();
	    }
	}
    }


    private void generateImage(Graphics2D g2d, String[] lines, Font[] fonts, Color[] colors, int width, int height, int lineSpacing, int extraLastLineSpacing)
    {

	// Lista para almacenar las líneas resultantes después de dividir
	List<String> wrappedLines = new ArrayList<>();
	List<Font> wrappedFonts = new ArrayList<>();
	List<Color> wrappedColors = new ArrayList<>();

	// Dividir las líneas si son demasiado largas
	for (int i = 0; i < lines.length; i++)
	{
	    Font font = fonts[i];
	    Color color = colors[i];
	    FontMetrics metrics = g2d.getFontMetrics(font);
	    String line = lines[i];
	    while (line.length() > 0)
	    {
		int breakIndex = findBreakIndex(line, metrics, width);
		wrappedLines.add(line.substring(0, breakIndex).trim());
		wrappedFonts.add(font);
		wrappedColors.add(color);
		line = line.substring(breakIndex).trim();
	    }
	}

	// Calcular el alto total del texto
	int totalTextHeight = 0;
	List<FontMetrics> wrappedMetrics = new ArrayList<>();
	for (int i = 0; i < wrappedLines.size(); i++)
	{
	    FontMetrics fm = g2d.getFontMetrics(wrappedFonts.get(i));
	    wrappedMetrics.add(fm);
	    totalTextHeight += fm.getHeight();
	    if(i < wrappedLines.size() - 2)
	    {
		totalTextHeight += lineSpacing;
	    }
	    else if(i == wrappedLines.size() - 2)
	    {
		totalTextHeight += extraLastLineSpacing;
	    }
	}

	// Calcular la posición inicial vertical para centrar las líneas
	int y = (height - totalTextHeight) / 2;

	// Dibujar cada línea de texto
	for (int i = 0; i < wrappedLines.size(); i++)
	{
	    g2d.setFont(wrappedFonts.get(i));
	    g2d.setColor(wrappedColors.get(i));
	    FontMetrics fm = wrappedMetrics.get(i);
	    String line = wrappedLines.get(i);
	    int x = (width - fm.stringWidth(line)) / 2;
	    y += fm.getAscent();
	    g2d.drawString(line, x, y);
	    y += fm.getDescent() + fm.getLeading();
	    if(i < wrappedLines.size() - 2)
	    {
		y += lineSpacing;
	    }
	    else if(i == wrappedLines.size() - 2)
	    {
		y += extraLastLineSpacing;
	    }
	}

    }


    // Encuentra el índice donde se debe dividir la línea
    private static int findBreakIndex(String line, FontMetrics metrics, int maxWidth)
    {

	int breakIndex = line.length();
	for (int i = 0; i < line.length(); i++)
	{
	    if(metrics.stringWidth(line.substring(0, i)) > maxWidth)
	    {
		breakIndex = i;
		break;
	    }
	}
	return breakIndex;
    }
}
