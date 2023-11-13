# Project Okapi

## Purpose
The goal of the project is to make an easy-to-use platform where students can
track their time spent studying and completed tasks.

## Installation
### Requirements
In order to get backend up and running, the system must have the following dependencies installed:
* OpenJDK 17

### Building
Note: Internet connectivity is required to download dependencies. Without it
the source will not compile.

From the root of the source, run:

`./mvnw clean install spring-boot:repackage`

After the task is completed, the `backend-SNAPSHOT` JAR file is created. In order to run it, open
from the `target` folder and run:

`java -jar  *.jar`

## Features

**Profile**
* Registers a user and saves his data in the database
* Logs in the user, checks if the provided password is correct
* Updates user data including when entering the correct password
* Deletes a user
* When requesting /{username}, gets public data (e.g. username, description, etc)

**Tasks**
* Creates task with unique id and saves it in the database
* Tasks have a name, description and status
* Updates task with new name and description and saves the change in the database
* Updates the status of the task
* Deletes the task


**Timer**
* Creates timer with unique id
* Starts timer and saves start time and end time in the database
* Stops timer, calculating the remaining time before the end time
* Updates the end time in database if the timer is continuing counting

# Usage
Every single requests is sent in a form `http://localhost:8080/api` + `{endpointName}`,
where endpoint is the one specified in the mapping.

**Profile**
```java
@GetMapping("/{username}")
public CustomerDTO getData(@PathVariable String username) {
    return customerService.getCustomerData(username);
}
```
```java
public CustomerDTO getCustomerData(String username) {
    Optional<Customer> dataShell = customerRepository.findByUsername(username);
    if (dataShell.isPresent()) {
        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(dataShell.get());
        eraseCustomerPassword(dto);
        return dto;
    }
    return null;
}
```
```java
@PostMapping("login")
public boolean login(@RequestBody CustomerDTO customer) {
    return customerService.login(customer);
}
```
```java
public boolean login(CustomerDTO customer) {
    return customerExists(customer.getUsername()) && validPassword(customer);
}
```
```java
@PostMapping("register")
public CustomerDTO registerCustomer(@RequestBody CustomerDTO customer) {
    return customerService.register(customer);
}
```
```java
public CustomerDTO register(CustomerDTO customer) {
    if (!customerExists(customer.getUsername())) {
        customerRepository.save(CustomerMapper.INSTANCE.toEntity(customer));
        return customer;
    }
    return null;
}

```
```java
@PostMapping("update/username")
public CustomerDTO updateCustomerUsername(@RequestBody CustomerDTO customer) {
    return customerService.updateUsername(customer);
}
```
```java
@PostMapping("update/password")
public CustomerDTO updateCustomerPassword(@RequestBody CustomerDTO customer) {
    return customerService.updatePassword(customer);
}
```
```java
public CustomerDTO update(CustomerDTO customer, CustomerServiceUpdate updateType) {
    Optional<Customer> customerOptional = customerRepository.findByUsername(customer.getUsername());
    if (customerOptional.isPresent()) {
        Customer dataShell = customerOptional.get();
        if (updateType.equals(CustomerServiceUpdate.CHANGE_USERNAME) && (validPassword(dataShell, customer.getPassword()))) {
                dataShell.setUsername(customer.getNewUsername());
                customer.setUsername(customer.getNewUsername());
                customerRepository.save(dataShell);

                return customer;

        }

        if (updateType.equals(CustomerServiceUpdate.CHANGE_PASSWORD) && (validPassword(dataShell, customer.getPassword()))) {
                dataShell.setPassword(customer.getNewPassword());
                customer.setPassword(customer.getNewPassword());
                customerRepository.save(dataShell);

                return customer;

        }
    }

    return null;
}

public CustomerDTO updateUsername(CustomerDTO customer) {
    // Check whether the desired username is taken
    if (customerExists(customer.getNewUsername())) {
        return null;
    }

    return update(customer, CustomerServiceUpdate.CHANGE_USERNAME);
}

public CustomerDTO updatePassword(CustomerDTO customer) {
    return update(customer, CustomerServiceUpdate.CHANGE_PASSWORD);
}

```
```java
@DeleteMapping("delete")
public CustomerDTO deleteCustomer(@RequestBody CustomerDTO customer) {
    return customerService.delete(customer);
}
```
```java
public CustomerDTO delete(CustomerDTO customer) {
    Optional<Customer> customerOptional = customerRepository.findByUsername(customer.getUsername());
    if (customerOptional.isPresent()) {
        Customer dataShell = customerOptional.get();
        if (validPassword(dataShell, customer.getPassword())) {
            customerRepository.delete(dataShell);
            return customer;
        }
    }

    return null;
}
```

**Tasks**
```java
@GetMapping
public List<TaskDTO> getAllTasks() {
    return taskService.getAllTasks();
}
```
```java
public List<TaskDTO> getAllTasks() {
    List<Task> task = (List<Task>) taskRepository.findAll();
    return task.stream()
    .map(TaskMapper.INSTANCE::toDTO)
    .collect(Collectors.toList());
}
```
```java
@GetMapping("/{id}")
public TaskDTO getTaskById(@PathVariable long id) {
    return taskService.getTaskById(id);
}
```
```java
public TaskDTO getTaskById(long id) {
    Optional<Task> task = taskRepository.findById(id);
    return task.map(TaskMapper.INSTANCE::toDTO).orElse(null);
}

```
```java
@PostMapping
public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
    return taskService.createTask(taskDTO);
}
```
```java
public TaskDTO createTask(TaskDTO taskDTO) {
    Task task = TaskMapper.INSTANCE.toEntity(taskDTO);
    task = taskRepository.save(task);
    return TaskMapper.INSTANCE.toDTO(task);
}

```
```java
@PutMapping("/{id}")
public TaskDTO updateTask(@PathVariable long id, @RequestBody TaskDTO taskDTO) {
    return taskService.updateTask(id, taskDTO);
}
```
```java
public TaskDTO updateTask(long id, TaskDTO task) {
    Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            Task updatedTask = taskRepository.save(existingTask);
            return TaskMapper.INSTANCE.toDTO(updatedTask);
        }
        return null;
}

```
```java
@PatchMapping("/{id}/status")
public TaskDTO updateTaskStatus(@PathVariable long id) {
    return taskService.statusTask(id);
}
```
```java
public TaskDTO statusTask(long id) {
    Optional<Task> optionalTasks = taskRepository.findById(id);
        if (optionalTasks.isPresent()) {
        Task existingTask = optionalTasks.get();
        TaskStatus status = existingTask.getStatus();
            if (status == TaskStatus.TODO) {
            existingTask.setStatus(TaskStatus.WORKING);
            } else if (status == TaskStatus.WORKING) {
            existingTask.setStatus(TaskStatus.DONE);
            } else if (status == TaskStatus.DONE) {
            existingTask.setStatus(TaskStatus.TODO);
            }
        taskRepository.save(existingTask);
        return TaskMapper.INSTANCE.toDTO(existingTask);
        }
    return null;
    }

```
```java
@DeleteMapping("/{id}")
public void deleteTask(@PathVariable long id) {
    taskService.deleteTask(id);
}
```
```java
public void deleteTask(long id) {
    taskRepository.deleteById(id);
}
```

**Timer**

```java
@GetMapping("/{id}")
public TimerDTO getTimer(@PathVariable Long id) {
    return timerService.getTimerById(id);
}
```
```java
public TimerDTO getTimerById(Long id) {
    return TimerMapper.INSTANCE.toDTO(timerRepository.findById(id).orElse(null));
}

```
```java

//curl -X POST http://localhost:8080/api/timer/start&id?=
@PostMapping("/start")
public TimerDTO startTimer(@RequestParam Long id) {
    return timerService.startTimer(id);
}
```
```java
public TimerDTO startTimer(Long id) {
    Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
        Timer timer = opTimer.get();
            if (timer.getRemainingTime() > 0) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime newEndTime = now.plusSeconds(timer.getRemainingTime());
            timer.setEndTime(newEndTime);
            } else {
            timer.setStartTime(LocalDateTime.now());
            timer.setEndTime(timer.getStartTime().plusSeconds(60));
            }
        timer.setRemainingTime(0);
        timerRepository.save(timer);
        return TimerMapper.INSTANCE.toDTO(timer);
        }
    return null;
}

```
```java
//curl -X POST http://localhost:8080/api/timer/stop&id?=
@PostMapping("/stop")
public TimerDTO stopTimer(@RequestParam Long id) {
    return timerService.stopTimer(id);
}
```
```java
public TimerDTO stopTimer(Long id) {
    Optional<Timer> opTimer = timerRepository.findById(id);
        if (opTimer.isPresent()) {
        Timer timer = opTimer.get();
        LocalDateTime now = LocalDateTime.now();
        long remainingTime =  ChronoUnit.SECONDS.between(now, timer.getEndTime());
            if (remainingTime > 0) {
            timer.setRemainingTime(remainingTime);
            } else {
            timer.setRemainingTime(0);
            }
        timerRepository.save(timer);
        return TimerMapper.INSTANCE.toDTO(timer);
        }
    return null;
}

```
```java
@PostMapping("/create")
public TimerDTO createTimer() {
    return timerService.createTimer();
}
```
```java
public TimerDTO createTimer() {
    TimerDTO timerDTO = new TimerDTO();
    timerDTO.setStartTime(LocalDateTime.now());
    timerDTO.setEndTime(timerDTO.getStartTime().plusSeconds(60));
    Timer timer = timerRepository.save(TimerMapper.INSTANCE.toEntity(timerDTO));
    timerDTO.setId(timer.getId());
    return timerDTO;
}
```



# Roadmap
Features we will finish developing until the end of project:
* User's *time spent studying* will be saved as a **total time**.
* Users will be able to form study/working groups
* Each study group will have a unified task management system
* Groups will have a scoreboard so students can compete with each other
* PassKey support to drop passwords altogether
* Quick Notes
* Offline support
* Mobile-first interface
* GitLab support (tasks = issues && automatic `/spent` when timer is stopped)
* Dynamic background (wallpaper changes according to student's preferences)
