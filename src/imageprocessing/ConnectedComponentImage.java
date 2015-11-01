package imageprocessing;


import java.awt.Color;
import java.util.Random;
import java.util.Scanner;

import edu.princeton.cs.introcs.Picture;

/*************************************************************************
 * Compilation: javac ConnectedComponentImage.java
 * 
 * The <tt>ConnectedComponentImage</tt> class
 * <p>
 * You do the rest....
 * 
 * @author 
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
		/*pic = new Picture(fileLocation);
		picture = binaryComponentImage(pic);
		picture.show();
		width = picture.width();
		height = picture.height();
		count = height*width;
		pixels = new int[height*width];
		id = new int[height*width];
		sz = new int[height*width];
		for(int i = 0; i < height*width; i++)
		{
			id[i] = i;
			sz[i] = 0;
		}*/
	}
	
	/**
	 * The main method that is called immediately when the program is run.
	 * Calls constructor and specifies file location.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		ConnectedComponentImage app = new ConnectedComponentImage();
		app.run();
	}
	
	public int initialMenu()
	{
		System.out.println("What image would you like to use?");
		System.out.println("---------------------------------");
		System.out.println("1) Shapes.bmp");
		System.out.println("2) Crosses.gif");
		System.out.println("3) Bacteria.bmp");
		System.out.println("4) Stars.jpg  (Warning: this is a large picture that takes a long time to process)");
		System.out.println("5) Stars2.jpg  (Warning: this is a large picture that takes a long time to process)");
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
	 * This method allows the user to choose what they would like to do with the picture.
	 * 
	 * 
	 */
	public void run()
	{
		int option = initialMenu();
		
		while(option != 1 && option != 2 && option != 3 && option != 4 && option != 5)
		{
			System.out.println("Invalid option entered. Please enter a number between 1 and 5");
			option = initialMenu();
		}
			switch(option)
			{
				case 1:
					pic = new Picture("images/shapes.bmp");
				break;
				case 2: 
					pic = new Picture("images/crosses.gif");
				break;
				case 3:
					pic = new Picture("images/bacteria.bmp");
				break;
				case 4:
					pic = new Picture("images/stars.jpg");
				break;
				case 5:
					pic = new Picture("images/stars2.jpg");
				break;
				default:
			
				break;
		}
		
			pic.show();
		width = pic.width();
		height = pic.height();
		picture = binaryComponentImage(pic);
		//picture.show();
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
		System.out.println(countComponents());
		//picture.show();
		
		main();
	}
	
	public void main()
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
					
				break;
				case 3:
					
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
	public int countComponents() {
		// TODO
		return count-1;// -1 for the background
	}
	
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
	
	public boolean connected(int p, int q) 
	{
			return root(p) == root(q);
	} 

	/**
	 * Returns the original image with each object bounded by a red box.
	 * 
	 * @return a picture object with all components surrounded by a red box
	 */
	public Picture identifyComonentImage() {

		return null;

	}

	/**
	 * Returns a picture with each object updated to a random colour.
	 * 
	 * @return a picture object with all components coloured.
	 */
	public Picture colourComponentImage() 
	{
		for(int x = 0; x < count; x++)
		{
			Random random = new Random();
			Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
		}
		
		return null;

	}

	public Picture getPicture() {
		// TODO Auto-generated method stub
		return picture;
	}
	
	

	/**
	 * Returns a binarised version of the original image
	 * 
	 * @return a picture object with all components surrounded by a red box //FIX THIS
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
	
	private void check(int r)
	{
		int N = id.length;
		if(r < 0 || r >= N)
		{
			throw new IndexOutOfBoundsException(" index " + r + " is not between 0 and " + (N-1));
		}
	}

}
