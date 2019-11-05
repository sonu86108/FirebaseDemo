package com.sonu.firebasedemo.models;

public class Word
{
		String word,meaning,desc,id;

		public Word(String id,String word, String meaning, String desc)
		{
				this.id=id;
				this.word = word;
				this.meaning = meaning;
				this.desc = desc;
		}
		public Word(String word, String meaning, String desc)
		{
				this.id=id;
				this.word = word;
				this.meaning = meaning;
				this.desc = desc;
		}
		
		public Word(){
				
		}
		public void setId(String id){
				this.id=id;
		}
		
		public String getId(){
				return id;
		}

		public void setWord(String word)
		{
				this.word = word;
		}

		public String getWord()
		{
				return word;
		}

		public void setMeaning(String meaning)
		{
				this.meaning = meaning;
		}

		public String getMeaning()
		{
				return meaning;
		}

		public void setDesc(String desc)
		{
				this.desc = desc;
		}

		public String getDesc()
		{
				return desc;
		}
}
