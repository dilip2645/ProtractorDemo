import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class CourseComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.xpath('//tr[2]/td[2]'));
  // editbutton = element.all(by.xpath('//tr[2]/td[2]'));

  // course_Name1 = element.all(by.xpath("//div[@class='table-responsive']//table//tbody/tr")).getWebElements();
  title = element(by.xpath("//h5[contains(text(),'Course')]"));

  // editButton = element(by.id('jh-edit-entity'));

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  // async clickOnEditButton(timeout?: number) {
  //   await this.editbutton.last().click();
  // }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getText();
    // return this.title;
  }
}

export class CourseUpdatePage {
  // pageTitle = element(by.id('jhi-course-heading'));
  pageTitle = element(by.xpath('//*/div/div[1]/h5'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  courseNameInput = element(by.id('field_courseName'));
  courseFeeInput = element(by.id('field_courseFee'));
  courseDurationInput = element(by.id('field_courseDuration'));

  async getPageTitle() {
    return this.pageTitle.getText();
    // return this.pageTitle;
  }

  // async getPageTitles() {
  //   // return this.pageTitle.getText();
  //   return this.pageTitles;
  // }

  async setCourseNameInput(courseName) {
    await this.courseNameInput.sendKeys(courseName);
  }

  async getCourseNameInput() {
    return await this.courseNameInput.getAttribute('value');
  }

  async setCourseFeeInput(courseFee) {
    await this.courseFeeInput.sendKeys(courseFee);
  }

  async getCourseFeeInput() {
    return await this.courseFeeInput.getAttribute('value');
  }

  async setCourseDurationInput(courseDuration) {
    await this.courseDurationInput.sendKeys(courseDuration);
  }

  async getCourseDurationInput() {
    return await this.courseDurationInput.getAttribute('value');
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CourseDeleteDialog {
  // private dialogTitle = element(by.id('jhi-delete-cource-heading'));
  private dialogTitle = element(by.xpath('//div[2]/div/p'));
  private confirmButton = element(by.id('jhi-confirm-delete-course'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
