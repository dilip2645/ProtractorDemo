import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class PersonDetailsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-person-details div table .btn-danger'));
  title = element.all(by.css('jhi-person-details div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getText();
  }
}

export class PersonDetailsUpdatePage {
  pageTitle = element(by.id('jhi-person-details-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  personNameInput = element(by.id('field_personName'));
  personIdInput = element(by.id('field_personId'));
  personAddressInput = element(by.id('field_personAddress'));

  async getPageTitle() {
    return this.pageTitle.getText();
  }

  async setPersonNameInput(personName) {
    await this.personNameInput.sendKeys(personName);
  }

  async getPersonNameInput() {
    return await this.personNameInput.getAttribute('value');
  }

  async setPersonIdInput(personId) {
    await this.personIdInput.sendKeys(personId);
  }

  async getPersonIdInput() {
    return await this.personIdInput.getAttribute('value');
  }

  async setPersonAddressInput(personAddress) {
    await this.personAddressInput.sendKeys(personAddress);
  }

  async getPersonAddressInput() {
    return await this.personAddressInput.getAttribute('value');
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

export class PersonDetailsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-personDetails-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-personDetails'));

  async getDialogTitle() {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
