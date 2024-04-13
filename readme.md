### 开发环境

Windows 10 64-bit
IntelliJ IDEA Community Edition 2022.3.3 x64

### 单词检索功能使用说明

1. 自动测试在"\Experiment1Test"文件夹下
2. 测试的脚本文件为'test.bat'，测试前需要修改：
    （1）set JAVA_HOME=  '此处为jdk存放路径'
    （2）set TO_BE_TEST_CLASSPAH=  '此处为被测试项目存放路径'
3. 修改完成后，运行test.bat 即可
4. 手工测试需要打开对于的项目文件，分别运行TestBuildIndex建立倒排索引后，再运行TestSearchIndex
5. 在运行TestSearchIndex的main()函数时，之间输入希望查询的单词即可。