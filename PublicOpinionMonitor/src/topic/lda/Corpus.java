package topic.lda;

/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/29 17:03</create-date>
 *
 * <copyright file="Corpus.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * a set of documents
 * 语料库，也就是文档集合
 *
 * @author hankcs
 */
public class Corpus
{
	//1. 每个元素为一个文件的内容.
	//2. 每个文件的内容中记录了该文件的每个词在map集合里的索引值.
    List<int[]> documentList;
    Vocabulary vocabulary;

    public Corpus()
    {
    	//1. 每个元素为一个文件的内容.
    	//2. 每个文件的内容中记录了该文件的每个词在map集合里的索引值.
        documentList = new LinkedList<int[]>();
        vocabulary = new Vocabulary();
    }

    public int[] addDocument(List<String> document)
    {
        int[] doc = new int[document.size()];
        int i = 0;
        for (String word : document)
        {
            doc[i++] = vocabulary.getId(word, true);
        }
        documentList.add(doc);
        return doc;
    }

    public int[][] toArray()
    {
        return documentList.toArray(new int[0][]);
    }

    public int getVocabularySize()
    {
        return vocabulary.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (int[] doc : documentList)
        {
            sb.append(Arrays.toString(doc)).append("\n");
        }
        sb.append(vocabulary);
        return sb.toString();
    }

    /**
     * Load documents from disk
     *
     * @param folderPath is a folder, which contains text documents.
     * @return a corpus
     * @throws IOException
     */
    public static Corpus load(String folderPath) throws IOException
    {
        Corpus corpus = new Corpus();
        File folder = new File(folderPath);
        for (File file : folder.listFiles())
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line;
            // wordList存储一个文件中所有的词
            List<String> wordList = new LinkedList<String>();
            while ((line = br.readLine()) != null)
            {
                String[] words = line.split(" ");
                for (String word : words)
                {
                    if (word.trim().length() < 2) continue;
                    wordList.add(word);
                }
            }
            br.close();
            //1. 将词和索引添加到一个map中.
            //2. 将词单独保存在string[]中.
            //3. 将索引保存在linkedlist<int[]>中.
            corpus.addDocument(wordList);
        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }
    
    /**
     * Load documents from list
     *
     * @param a list contain documents, a element is a document.
     * @return a corpus or null if list useless.
     * @throws IOException
     */
    public static Corpus load(BlockingQueue<String> docs)
    {
        Corpus corpus = new Corpus();
        for (String doc : docs)
        {
            // wordList存储一个文件中所有的词
            List<String> wordList = new LinkedList<String>();
            String[] words = doc.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                wordList.add(word);
            }

            //1. 将词和索引添加到一个map中.
            //2. 将词单独保存在string[]中.
            //3. 将索引保存在linkedlist<int[]>中.
            corpus.addDocument(wordList);
        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }

    /**
     * Load documents from parameter
     *
     * @param data is text documents.
     * @return a corpus
     */
    public static Corpus loading(String data)
    {
        Corpus corpus = new Corpus();
        List<String> wordList = new LinkedList<String>();
        String[] words = data.split(" ");
        
        for (String word : words)
        {
            if (word.trim().length() < 2) continue;
            wordList.add(word);
        }
        
        //1. 将词和索引添加到一个map中.
        //2. 将词单独保存在string[]中.
        //3. 将索引保存在linkedlist<int[]>中.
        corpus.addDocument(wordList);
        
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;        
    }
    
    public Vocabulary getVocabulary()
    {
        return vocabulary;
    }

    public int[][] getDocument()
    {
        return toArray();
    }

    public static int[] loadDocument(String path, Vocabulary vocabulary) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        List<Integer> wordList = new LinkedList<Integer>();
        while ((line = br.readLine()) != null)
        {
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                Integer id = vocabulary.getId(word);
                if (id != null)
                    wordList.add(id);
            }
        }
        br.close();
        int[] result = new int[wordList.size()];
        int i = 0;
        for (Integer integer : wordList)
        {
            result[i++] = integer;
        }
        return result;
    }
}