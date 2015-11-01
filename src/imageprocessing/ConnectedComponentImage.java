package imageprocessing;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import edu.princeton.cs.introcs.Picture;

/*************************************************************************
 * Compilation: javac ConnectedComponentImage.java
 * 
 * The <tt>ConnectedComponentImage</tt> class
 * <p>
 * 
 * @author Chris Bodels
 *************************************************************************/
public class ConnectedComponentImage 
{

	private Picture pic;
	private Picture picture;
	private int height, width, count;
	private int[] id, sz;
	private Scanner input;

	/**
	 * Initialise fields
	 * 
	 * 
	 */
	public ConnectedComponentImage() 
	{
		input = new Scanner(System.in);
	}
	
	/**
	 * The main method that is called immediately when the program is run.
	 * Calls constructor.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		ConnectedComponentImage app = new ConnectedComponentImage();
		app.run();
	}
	
	/**
	 * This method is called at the start of the run method and displays the text for 
	 * the menu that allows the user to select what image they would like to use.
	 * 
	 * @return The user's choice (an integer between one and 5)
	 */
	public int initialMenu()
	{
		System.out.println("What image would you like to use?");
		System.out.println("---------------------------------");
		System.out.println("1) Shapes.bmp  (Works well");
		System.out.println("2) Bacteria.bmp  (Works well)");
		System.out.println("3) Stars.jpg  (Warning: this is a large picture that takes a long time to process)");
		System.out.println("4) Stars2.jpg  (Warning: this is a large picture that takes a long time to process)");
		int option = 0;
		boolean notGoodInput = false;
		do
		{
			try {
			option = input.nextInt();
			notGoodInput = true;
			}
			catch(Exception e) {
				String throwOut = input.nextLine();
				System.out.println("Number expected, you entered text. (Stop trying to break my code)");
			}
		}while(!notGoodInput);
		return option;
	}
	
	/**
	 * This method displays the text for the second menu of the program that allows
	 * the user to choose what they would like to do with the chosen picture.
	 * 
	 * @return The user's choice (an integer between 0 and 3)
	 */
	public int secondMenu()
	{
		System.out.println("What would you like to do with this image?");
		System.out.println("---------------------------------");
		System.out.println("1) Return number of components in image");
		System.out.println("2) Return image with components highlighted with a red bounding box");
		System.out.println("3) Return image with each component randomly coloured");
		System.out.println("0) Exit");
		int option = 0;
		boolean notGoodInput = false;
		do
		{
			try {
			option = input.nextInt();
			notGoodInput = true;
			}
			catch(Exception e) {
				String throwOut = input.nextLine();
				input.next();
				System.out.println("Number expected, you entered text. (Stop trying to break my code)");
			}
		}while(!notGoodInput);
		return option;
	}
	
	/**
	 * This method allows the user to choose what picture they will use in the program.
	 * It also initializes some of the instance variables and unions pixels in the image
	 * in order to be able to get a count and manipulate it later.
	 */
	public void run()
	{
		int option = initialMenu();
		
		while(option != 1 && option != 2 && option != 3 && option != 4)
		{
			System.out.println("Invalid option entered. Please enter a number between 1 and 4");
			option = initialMenu();
		}
			switch(option)
			{
				case 1:
					pic = new Picture("images/shapes.bmp");
				break;
				case 2:
					pic = new Picture("images/bacteria.bmp");
				break;
				case 3:
					pic = new Picture("images/stars.jpg");
				break;
				case 4:
					pic = new Picture("images/stars2.jpg");
				break;
				default:
			
				break;
		}
		width = pic.width();
		height = pic.height();
		picture = binaryComponentImage(pic);
		count = height*width;
		id = new int[height*width];
		sz = new int[height*width];
		for(int i = 0; i < height*width; i++)
		{
			id[i] = i;
			sz[i] = 0;
		}

		Color p;
		Color q = picture.get(1, 0);
		Color r = picture.get(0, 1);
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
					p = picture.get(x,  y);
					if((x+1) < width)
					{
						q = picture.get((x+1), y);
						if(Luminance.lum(p) == Luminance.lum(q) && connected((y*width+x), (y*width+(x+1))) == false)
						{
							union((y*width+x), (y*width+(x+1)));
						}
					}
					if((y+1) < height)
					{
						r = picture.get(x, (y+1));
						if(Luminance.lum(p) == Luminance.lum(r) && connected((y*width+x), ((y+1)*width+x)) == false)
						{
							union((y*width+x), (y+1)*width+x);
						}
					}
			}
		}
		mainMenu();
	}
	
	/**
	 * This method is the main method of the program which reappears until the program is 
	 * terminated. It allows the user to choose what they would like to do with the image.
	 * 
	 */
	public void mainMenu()
	{
		int option = secondMenu();
		
		while(option != 0)
		{
			while(option != 1 && option != 2 && option != 3)
			{
				System.out.println("Invalid option entered. Please enter a number between 0 and 3");
				option = secondMenu();
			}
			
			switch(option)
			{
				case 1:
					System.out.println("");
					System.out.println("Number of components in image is: " + countComponents());
					System.out.println("");
					System.out.println("Press any key to continue...");
					input.next();
				break;
				case 2:
					System.out.println("");
					System.out.println("");
					System.out.println("Processing...");
					highlightComponentImage().show();
					System.out.println("");
					System.out.println("");
					System.out.println("");
					System.out.println("");
					System.out.println("Done!");
					System.out.println("");
					System.out.println("Press any key to continue...");
					input.next();
				break;
				case 3:
					System.out.println("");
					System.out.println("");
					System.out.println("Processing...");
					colourComponentImage().show();
					System.out.println("");
					System.out.println("");
					System.out.println("");
					System.out.println("");
					System.out.println("Done!");
					System.out.println("");
					System.out.println("Press any key to continue...");
					input.next();
				break;
				default:
				
				break;
			}
			option = secondMenu();
		}
	}

	/**
	 * Returns the number of components identified in the image.
	 * 
	 * @return the number of components (between 1 and N)
	 */
	public int countComponents() 
	{
		return count-1;// -1 for the background
	}
	
	/**
	 * Unions two pixels so that they are part of the same component.
	 * 
	 * @param p the first pixel to be unioned
	 * @param q the second pixel to be unioned
	 */
	public void union(int p, int q)
	{
		 int i = root(q), j = root(p);
		 if (sz[i] < sz[j]) 
		 {
			 id[i] = j; 
			 sz[j] += sz[i]; 
		 } 
		 else 
		 { 
			 id[j] = i;
			 sz[i] += sz[j]; 
		 } 
		 count--;
	}
	
	/**
	 * Gets the root of pixel i which is used to identify what component it is in.
	 * 
	 * @param i the pixel to be checked
	 * @return the root of the component that i is in
	 */
	private int root(int i) 
	{
		check(i);
		while (i != id[i]) 
		{
			id[i] = id[id[i]];
			i = id[i];
		}
		return i;
	}  
	
	/**
	 * Returns true if the two pixels being checked have the same root and are therefore 
	 * in the same component.
	 * 
	 * @param p the first pixel to be checked
	 * @param q the second pixel to be checked
	 * @return true if the two pixels have the same root and false if they are not
	 */
	public boolean connected(int p, int q) 
	{
			return root(p) == root(q);
	} 

	/**
	 * Returns a picture with each object updated to a random colour.
	 * 
	 * @return a picture object with all components coloured.
	 */
	public Picture colourComponentImage() 
	{
		Picture coloredPic = pic;
		ArrayList<Color> colors = new ArrayList<Color>(); 
		Random random = new Random();
		for(int x = 0; x < height*width; x++)
		{
			colors.add(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		}
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				int currentId = root(y*width+x);
				
				for(int i = 0; i < height*width; i++)
				{
					if(id[i] == currentId)
						coloredPic.set(x, y, colors.get(root(y*width+x)));
						
				}
			}
		}
		return coloredPic;
	}

	/**
	 * Returns the original, unmodified picture that was chosen at the start of the program.
	 * 
	 * @return a picture object of the original picture
	 */
	public Picture getPicture() {
		return pic;
	}
	
	/**
	 * Returns the original image with each object bounded by a red box.
	 * 
	 * @return a picture object with all components surrounded by a red box
	 */
	public Picture highlightComponentImage()
	{
		Picture highlightedPic = pic;
		for(int z = 0; z < height*width; z++)
		{
			int maxX = 0;
			int minX = width;
			int maxY = 0;
			int minY = height;
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					if(root(y*width+x) == id[z])
					{
						if(x < minX)
							minX = x;
						if(x > maxX)
							maxX = x;
						if(y < minY)
							minY = y;
						if(y > maxY)
							maxY = y;
					}
				}
			}
			for(int x = minX; x <= maxX; x++)
			{
				highlightedPic.set(x, minY, Color.RED);
				highlightedPic.set(x, maxY, Color.RED);
			}
			for(int y = minY; y <= maxY; y++)
			{
				highlightedPic.set(minX, y, Color.RED);
				highlightedPic.set(maxX,  y,  Color.RED);
			}
		}
		return highlightedPic;
	}

	/**
	 * Returns a binarised version of the original image
	 * 
	 * @return a picture object that has been binarised
	 */
	public Picture binaryComponentImage(Picture newPic) 
	{
		double thresholdPixelValue = 128.0;
		Picture bAndWImage = newPic; 
		
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				Color c = bAndWImage.get(x,y);
				Color gray = Luminance.toGray(c);
				bAndWImage.set(x, y, gray);
			}
		}
		
		for(int x=0; x < width; x++)
		{
			for(int y=0; y < height; y++)
			{
				Color c = bAndWImage.get(x, y);
				if(Luminance.lum(c) < thresholdPixelValue)
				{
					bAndWImage.set(x, y, Color.BLACK);
				}
				else
				{
					bAndWImage.set(x, y, Color.WHITE);
				}
			}
		}
		return bAndWImage;
	}
	
	/**
	 * Used to check that the pixel being checked is in the array to avoid ArrayIndexOutOfBoundsExceptions
	 * 
	 * @param r the pixel being checked
	 */
	private void check(int r)
	{
		int N = id.length;
		if(r < 0 || r >= N)
		{
			throw new IndexOutOfBoundsException(" index " + r + " is not between 0 and " + (N-1));
		}
	}

}
