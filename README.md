# User Guide for Mirai

> "Come on! It is just 8 in the morning..."\
> "As long as I am here, you will not return to that sedentary lifestyle again."\
> "Ugh, fine... Wait, you are not fictional?"\
> "Well, not anymore 😉"

Thanks for using Mirai, a (beautiful and intelligent assistant, as well as a) **desktop chatbot application** that helps you track your tasks and makes your life easier!

Mirai is optimised for use via a **Command Line Interface** (CLI), while stil having the benefits of a **Graphical User Interface** (GUI). If you can type fast, Mirai will
get your job done much faster than traditional GUI applications.

![Mirai chatbot application interface](Ui.png)

In case you wonder about Mirai's background story, visit our story [here](https://www.meganovel.com/story/A-Backpack-a-Gun-and-a-Codex_31000344924/Prologue_2762250)!

# Quick Start
1. Ensure you have Java `17` or above installed in your computer.
2. Download the latest `mirai.jar` file from [here](https://github.com/Masunori/ip/releases/download/A-CheckStyle/mirai.jar).
3. Copy the file to the folder you want to use as the *home folder* for your Mirai. From here on, assume that you store your file at `C:/Users/Username`.
4. Open a command terminal, then key in the following:
```sh
cd C:/Users/User
java -jar addressbook.jar
```
You should see the interface that looks like below.

![Mirai chatbot application welcome interface](Welcome.png)

5. Type a command in the command box, and press **Enter** or click on the Send button to send a message to Mirai. Some necessary commands are below:
   - `list`: Lists all tasks.
   - `todo sleep`: Adds a to-do task with the description "sleep" into the task list.
   - `deadline submit homework /by 2025-02-20 1559`: Adds a deadline task with description "submit homework" and deadline 15:59, 20th February 2025 into the task list.
   - `event meeting /from 2025-02-20 1600 /to 2025-02-20 1800`: Adds an event with description "meeting" and lasts from 16:00 to 18:00, 20th February, 2025 into the task list.
   - `bye`: Exits Mirai chatbot application.
6. Refer to the Commands section below for all supported commands.

# Commands
> [!NOTE]
> - Words in square brackets are parameters to be supplied by the user.
>   e.g. in `todo [description]`, `[description]` is a parameter the user has to supply, such as `todo sleep`.
>   
> - For commands that involve datetimes, Mirai supports the following date formats:
>   - `DD/MM/YYYY HHmm`, such as `31/01/2025 1559`
>   - `DD/MM/YYYY HH:mm`, such as `31/01/2025 15:59`
>   - `DD/MM/YY HHmm`, such as `31/01/25 1559`
>   - `DD/MM/YY HH:mm`, such as `31/01/25 15:59`
>   - `YYYY-MM-DD HHmm`, such as `2025-01-31 1559`
>   - `YYYY-MM-DD HH:mm`, such as `2025-01-31 15:59`
>  
> - For commands that involve indexes, to know the index of a task, use the `list` command, and look until you see the index of your desired task.

| Keyword | What it does | How to use it |
| ------- | ------------ | ------------- |
| `bye`   | Exits Mirai chatbot application. | `bye` |
| `deadline` | Adds a deadline task into the task list. | `deadline [description] /by [datetime]` |
| `delete` | Deletes a task from the storage with the specified index. | `delete [index]` |
| `event` | Adds an event task into the task list. | `event [description] /from [start datetime] /to [end datetime]` |
| `find` | Finds a task based on a word/words. Note that everything after the `find` keyword will be treated as one block.<br><br> For example, `find read book` will retrieve all tasks containing the `read book` phrase exactly. | `find [keyword(s)]` |
| `flexfind` | Sorts all tasks based on the decreasing level of revelance to the keyword(s). Each word is treated separately.<br><br> For example `flexfind read book` will check for closeness of the task description with respect to `read` and `book` separately, so `flexfind book read` will also produce the same results, even if it does not make as much semantic meaning. | `flexfind [keyword(s)]` |
| `help` | Lists all commands that Mirai supports. | `help` |
| `list` | Lists all tasks in the task list. | `list` |
| `mark` | Marks a task with a specified index as done. | `mark [index]` |
| `todo` | Adds a to-do task into the task list. | `todo [description]` |
| `unmark` | Marks a task with a specified index as incompleted. | `unmark [index]` |

# Non-command features
### Saving data
Mirai's data is saved in the hard disk automatically after any command that alters the data. You do not need to save manually!

### Editing the data file
Task data is saved in a `.txt` file named `mirai.txt`, at `[address of the .jar file]/data/mirai.txt`. Here, tasks are formatted as
| Task type | Syntax | Example |
| --------- | ------ | ------- |
| Todo      | `T \| 0 \| task description` | `T \| 0 \| sleep` |
| Deadline  | `D \| 0 \| task description \| YYYY-MM-DDTHH:mm` | `D \| 1 \| submit homework \| 2025-02-20T23:59` |
| Event     | `E \| 0 \| task description \| YYYY-MM-DDTHH:mm \| YYYY-MM-DDTHH:mm` | `E \| 0 \| meeting \| 2025-02-21T16:00 \| 2025-02-21T18:00` |

Here, content blocks are separated by vertical bars (`|`).
- (**COMMON**) The first content block is the task identifier (`T` for todo, `D` for deadline, and `E` for events)
- (**COMMON**) The second content block is the completion status of the task (`0` for incompleted, `1` for done)
- (**COMMON**) The third content block is the task description. Task descriptions can be more than 1 word, separated with space
- (**Exclusive to DEADLINE and EVENT**) The fourth content block is the date-time.
  - For a deadline task, it is the deadline date-time.
  - For an event task, it is the date-time that the event starts.
- (**Exclusive to EVENT**) The fifth content block is the date-time that an event task ends.

Advanced users are welcome to directly update the data by editing this data file.

# FAQ
**Q**: How do I transfer my data to another computer.\
**A**: Install `mirai.jar` in the other computer. Overwrite the empty `mirai.jar` data file it creates with the file in your current computer. 
