package com.gilded.gbjam;



public class TitleScreen extends Screen
{
    public TitleScreen()
    {
    }

    @Override
    public void render()
    {
		getSpriteBatch().begin();
    	draw(Art.splashScreen, 0, 0);
		getSpriteBatch().end();
    }
    
	@Override
	public void tick(Input input) {
		if(input.buttonStack.peek() == Input.ACTION) {
			this.setScreen(new InGameScreen());
			input.buttonStack.delete(Input.ACTION);
		}
	}
}
