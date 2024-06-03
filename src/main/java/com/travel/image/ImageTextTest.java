package com.travel.image;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class ImageTextTest
{

    public static void main(String[] args)
    {

	// Dimensiones de la imagen
	int width = 858;
	int height = 613;
	int lineSpacing = 10; // Espacio entre líneas
	int extraLastLineSpacing = 40; // Espacio entre líneas

	// Crear una imagen en blanco
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = image.createGraphics();

	// Establecer el color de fondo
	g2d.setColor(Color.WHITE);
	g2d.fillRect(0, 0, width, height);

	// Configurar el texto
	String[] lines =
	{ "Julio Costa Asturias", "Hotel 4* frente al mar", "cancelable por 36 € asdf fd sdf asdf sdf sd fs df a fsdfsdfsdf", "www.exprimeviajes.com" };
	Font[] fonts =
	{ new Font("Arial", Font.BOLD, 42), new Font("Times New Roman", Font.BOLD, 28), new Font("Courier New", Font.BOLD, 32), new Font("Courier New", Font.ITALIC, 22) };
	Color[] colors =
	{ Color.BLACK, Color.BLUE, Color.RED, Color.GREEN };

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

	// Liberar recursos
	g2d.dispose();

	// Guardar la imagen
	try
	{
	    ImageIO.write(image, "png", new File("centered_text.png"));
	    System.out.println("Imagen guardada como centered_text.png");
	}
	catch (IOException ex)
	{
	    ex.printStackTrace();
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
